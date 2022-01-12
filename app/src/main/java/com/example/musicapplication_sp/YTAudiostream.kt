package com.example.musicapplication_sp

import kotlin.jvm.JvmStatic
import org.apache.http.NameValuePair
import org.apache.http.client.CookieStore
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.client.utils.URIBuilder
import org.apache.http.client.utils.URLEncodedUtils
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicNameValuePair
import org.apache.http.protocol.BasicHttpContext
import org.apache.http.protocol.HttpContext
import java.io.*
import java.net.URI
import java.net.URISyntaxException
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.logging.Formatter
import java.util.logging.Level
import java.util.logging.LogRecord
import java.util.logging.Logger
import java.util.regex.Pattern
import kotlin.Throws

object YTAudiostream {
    var newline = System.getProperty("line.separator")
    private val log = Logger.getLogger(YTAudiostream::class.java.canonicalName)
    private val defaultLogLevelSelf = Level.FINER
    private val defaultLogLevel = Level.WARNING
    private val rootlog = Logger.getLogger("")
    private const val scheme = "http"
    private const val host = "www.youtube.com"
    private lateinit var uri: URIBuilder
    private val commaPattern = Pattern.compile(",")
    private val pipePattern = Pattern.compile("\\|")
    private val ILLEGAL_FILENAME_CHARACTERS = charArrayOf(
        '/',
        '\n',
        '\r',
        '\t',
        '\u0000',
        '`',
        '?',
        '*',
        '\\',
        '<',
        '>',
        '|',
        '\"',
        ':'
    )

    private fun usage(error: String?) {
        if (error != null) {
            System.err.println("Error: $error")
        }
        System.err.println("usage: JavaYoutubeDownload VIDEO_ID DESTINATION_DIRECTORY")
        System.exit(-1)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        if (args == null || args.size == 0) {
            usage("Missing video id. Extract from http://www.youtube.com/watch?v=VIDEO_ID")
        }
        try {
            setupLogging()
            log.fine("Starting")
            var videoId: String? = null
            var outdir = "."
            if (args.size == 1) {
                videoId = args[0]
            } else if (args.size == 2) {
                videoId = args[0]
                outdir = args[1]
            }
            val format = 18 // http://en.wikipedia.org/wiki/YouTube#Quality_and_codecs
            val encoding = "UTF-8"
            val userAgent = "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13"
            val outputDir = File(outdir)
            val extension = getExtension(format)
            play(videoId, format, encoding, userAgent, outputDir, extension)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        log.fine("Finished")
    }

    private fun getExtension(format: Int): String {
        return "mp4"
    }

    @Throws(Throwable::class)
    private fun play(
        videoId: String?,
        format: Int,
        encoding: String,
        userAgent: String,
        outputdir: File,
        extension: String
    ) {
        log.fine("Retrieving $videoId")
        val qparams: MutableList<NameValuePair> = ArrayList()
        qparams.add(BasicNameValuePair("video_id", videoId))
        qparams.add(BasicNameValuePair("fmt", "" + format))
        val uri = getUri("get_video_info", qparams)
        val cookieStore: CookieStore = BasicCookieStore()
        val localContext: HttpContext = BasicHttpContext()
        localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore)
        val httpclient: HttpClient = HttpClientBuilder.create().build()
        val httpget = HttpGet(uri)
        httpget.setHeader("User-Agent", userAgent)
        log.finer("Executing $uri")
        val response = httpclient.execute(httpget, localContext)
        val entity = response.entity
        if (entity != null && response.statusLine.statusCode == 200) {
            val instream = entity.content
            val videoInfo = getStringFromInputStream(encoding, instream)
            if (videoInfo != null && videoInfo.length > 0) {
                val infoMap: List<NameValuePair> = ArrayList()
                URLEncodedUtils.parse(infoMap, Scanner(videoInfo), encoding)
                var token: String? = null
                var downloadUrl: String? = null
                var filename = videoId
                for (pair in infoMap) {
                    val key = pair.name
                    val `val` = pair.value
                    log.finest("$key=$`val`")
                    if (key == "token") {
                        token = `val`
                    } else if (key == "title") {
                        filename = `val`
                    } else if (key == "fmt_url_map") {
                        val formats = commaPattern.split(`val`)
                        for (fmt in formats) {
                            val fmtPieces = pipePattern.split(fmt)
                            if (fmtPieces.size == 2) {
                                // in the end, download somethin!
                                downloadUrl = fmtPieces[1]
                                val pieceFormat = fmtPieces[0].toInt()
                                if (pieceFormat == format) {
                                    // found what we want
                                    downloadUrl = fmtPieces[1]
                                    break
                                }
                            }
                        }
                    }
                }
                filename = cleanFilename(filename)
                if (filename!!.isEmpty()) {
                    filename = videoId
                } else {
                    filename += "_$videoId"
                }
                filename += ".$extension"
                val outputfile = File(outputdir, filename)
                if (downloadUrl != null) {
                    downloadWithHttpClient(userAgent, downloadUrl, outputfile)
                }
            }
        }
    }

    @Throws(Throwable::class)
    private fun downloadWithHttpClient(userAgent: String, downloadUrl: String, outputfile: File) {
        val httpget2 = HttpGet(downloadUrl)
        httpget2.setHeader("User-Agent", userAgent)
        log.finer("Executing " + httpget2.uri)
        val httpclient2: HttpClient = HttpClientBuilder.create().build()
        val response2 = httpclient2.execute(httpget2)
        val entity2 = response2.entity
        if (entity2 != null && response2.statusLine.statusCode == 200) {
            val length = entity2.contentLength
            val instream2 = entity2.content
            log.finer("Writing $length bytes to $outputfile")
            if (outputfile.exists()) {
                outputfile.delete()
            }
            val outstream = FileOutputStream(outputfile)
            try {
                val buffer = ByteArray(2048)
                var count = -1
                while (instream2.read(buffer).also { count = it } != -1) {
                    outstream.write(buffer, 0, count)
                }
                outstream.flush()
            } finally {
                outstream.close()
            }
        }
    }

    private fun cleanFilename(filename: String?): String? {
        var filename = filename
        for (c in ILLEGAL_FILENAME_CHARACTERS) {
            filename = filename!!.replace(c, '_')
        }
        return filename
    }

    @Throws(URISyntaxException::class)
    private fun getUri(
        path: String,
        qparams: List<NameValuePair>,
    ): URI {
        return URIBuilder().setScheme(scheme)
            .setHost(host)
            .setPort(-1)
            .setPath("/$path")
            .setFragment(null)
            .setCharset(StandardCharsets.UTF_8)
            .build()

    }

    private fun setupLogging() {
        changeFormatter(object : Formatter() {
            override fun format(arg0: LogRecord): String {
                return arg0.message + newline
            }
        })
        explicitlySetAllLogging(Level.FINER)
    }

    private fun changeFormatter(formatter: Formatter) {
        val handlers = rootlog.handlers
        for (handler in handlers) {
            handler.formatter = formatter
        }
    }

    private fun explicitlySetAllLogging(level: Level) {
        rootlog.level = Level.ALL
        for (handler in rootlog.handlers) {
            handler.level = defaultLogLevelSelf
        }
        log.level = level
        rootlog.level = defaultLogLevel
    }

    @Throws(UnsupportedEncodingException::class, IOException::class)
    private fun getStringFromInputStream(
        encoding: String,
        instream: InputStream
    ): String {
        val writer: Writer = StringWriter()
        val buffer = CharArray(1024)
        try {
            val reader: Reader =
                BufferedReader(InputStreamReader(instream, encoding))
            var n: Int
            while (reader.read(buffer).also { n = it } != -1) {
                writer.write(buffer, 0, n)
            }
        } finally {
            instream.close()
        }
        return writer.toString()
    }
}