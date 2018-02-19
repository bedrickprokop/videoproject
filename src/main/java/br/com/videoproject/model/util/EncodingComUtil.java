package br.com.videoproject.model.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class EncodingComUtil {

    private static final String userID = "";
    private static final String userKey = "";

    public static String getStatus(String mediaId) {
        StringBuffer xml = new StringBuffer();
        xml.append("<?xml version='1.0'?>");
        xml.append("<query>");
        xml.append("<userid>" + userID + "</userid>");
        xml.append("<userkey>" + userKey + "</userkey>");
        xml.append("<action>getStatus</action>");
        xml.append("<mediaid>" + mediaId + "</mediaid>");
        xml.append("</query>");

        String result = makeRequest(xml);

        String status = result.substring(result.indexOf("<status>"),
                result.indexOf("</status>"));
        if (status.equalsIgnoreCase("FINISHED")) {
            String destination = result.substring(result.indexOf("<s3_destination>" + 16),
                    result.indexOf("</s3_destination>"));
            return destination;
        }
        return "";
    }

    public static String addMedia(String source) {
        String action = "addMedia";

        StringBuffer xml = new StringBuffer();
        xml.append("<?xml version='1.0'?>");
        xml.append("<query>");
        xml.append("<userid>" + userID + "</userid>");
        xml.append("<userkey>" + userKey + "</userkey>");
        xml.append("<action>" + action + "</action>");
        xml.append("<source>" + source + "</source>");
        xml.append("<format>\n" +
                "        <output>mp4</output>\n" +
                "        <size>0x240</size>\n" +
                "        <bitrate>350k</bitrate>\n" +
                "        <audio_bitrate>64k</audio_bitrate>\n" +
                "        <audio_sample_rate>44100</audio_sample_rate>\n" +
                "        <audio_channels_number>2</audio_channels_number>\n" +
                "        <framerate>24</framerate>\n" +
                "        <keep_aspect_ratio>yes</keep_aspect_ratio>\n" +
                "        <video_codec>libx264</video_codec>\n" +
                "        <profile>baseline</profile>\n" +
                "        <audio_codec>dolby_aac</audio_codec>\n" +
                "        <two_pass>no</two_pass>\n" +
                "        <turbo>no</turbo>\n" +
                "        <twin_turbo>no</twin_turbo>\n" +
                "        <cbr>no</cbr>\n" +
                "        <deinterlacing>auto</deinterlacing>\n" +
                "        <keyframe>120</keyframe>\n" +
                "        <audio_volume>100</audio_volume>\n" +
                "        <rotate>def</rotate>\n" +
                "        <metadata_copy>no</metadata_copy>\n" +
                "        <strip_chapters>no</strip_chapters>\n" +
                "        <hint>no</hint>\n" +
                "    </format>\"");
        xml.append("</query>");

        String result = makeRequest(xml);
        String mediaId = result.substring(result.indexOf("<MediaID>") + 9,
                result.indexOf("</MediaID>"));
        return mediaId;
    }

    private static String makeRequest(StringBuffer xml) {
        URL server = null;

        try {
            String url = "https://manage.encoding.com";
            System.out.println("Connecting to:" + url);
            server = new URL(url);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        try {
            String sRequest = "xml=" + URLEncoder.encode(xml.toString(), "UTF8");

            System.out.println("Open new connection to tunnel");
            HttpURLConnection urlConnection = (HttpURLConnection) server.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(60000);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            out.write(sRequest);
            out.flush();
            out.close();
            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();
            String str = urlConnection.getResponseMessage();
            System.out.println("Response:" + urlConnection.getResponseCode());
            System.out.println("Response:" + urlConnection.getResponseMessage());

            StringBuffer strbuf = new StringBuffer();
            byte[] buffer = new byte[1024 * 4];

            try {
                int n = 0;
                while (-1 != (n = is.read(buffer))) {
                    strbuf.append(new String(buffer, 0, n));
                }

                is.close();
                System.out.println(strbuf.toString());
                return strbuf.toString();

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
