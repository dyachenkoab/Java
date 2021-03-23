package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Act {
    private Document doc;
    private Elements imageElems;
    private Elements linksElems;
    private ListIterator<Element> imgIter = null;
    private HashSet<String> hash = new HashSet<>();
    private String siteName = "";
    private String baseURL = "";
    private String outputFolder = "";
    private boolean firstTime = true;
    private long imageMinSize = 1;
    private long deep = 0; //глубина
    private long imageCurrSize = 0;
    private int imgNumber = 0; //номер изображения

    public Act() {
        try {
            init();
            getHTML(siteName);
        } catch (IOException | ParseException | RuntimeException e) {
            System.out.println("Look at the config!");
            e.printStackTrace();
        }
    }

    private void init() throws IOException, ParseException {
        try {
            FileReader reader = new FileReader("config.json");
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            siteName = (String) jsonObject.get("url");
            deep = (long) jsonObject.get("deep");
            imageMinSize = (long) jsonObject.get("minSize") * 1048576; //количество байт в мб
            String str = (String) jsonObject.get("output");
            outputFolder = !str.endsWith("/") ? str : str + "/";

            Pattern pattern = Pattern.compile("((http)(s?)(.){3}([a-zA-Z0-9-_.]+(\\/)?))");
            //выделяем базовый url для отсеивания других сайтов
            Matcher matcher = pattern.matcher(siteName);
            matcher.find();
            baseURL = siteName.substring(matcher.start(), matcher.end()).endsWith("/") ? siteName.substring(matcher.start(), matcher.end()) : siteName.substring(matcher.start(), matcher.end()) + "/";
            System.out.println(baseURL + " BASE URL");

        } catch (IOException | ParseException | RuntimeException e) {
            throw e;
        }
    }

    private void getHTML(String url) throws IOException {
        HashSet<String> tempHash = new HashSet<>();

        System.out.println("/-----------TRY TO CONNECT " + deep + " LEVEL" + " --------------------------------------------/");
        try {
            //принадлежит ли к этому сайту
            if (url.startsWith(baseURL)) {

                doc = Jsoup.connect(url).get();
                System.out.println(doc.baseUri() + " <------ CURRENT URL");

                System.out.println("/------------------------------------------------------------------------------------/");

                linksElems = doc.select("a");
                imageElems = doc.select("img");

                imgIter = imageElems.listIterator();

                getImg(imgIter, doc);

                if (deep < 2) {
                    for (Element x : linksElems) {
                        System.out.println(x.attr("href") + " Link by attribute");

                        String link = x.attr("href");

                        if (!link.startsWith("http")) { //для относительных ссылок
                            link = link.startsWith("/") ? baseURL + link.substring(1) : baseURL + link;
                            System.out.println(link + " STING LINK");
                        }

                        if (!baseURL.equals(link) && !doc.baseUri().equals(link) && !baseURL.equals(link + "#") && !baseURL.equals(link + "/#")) {
                            //не добавляем самих себя
                            System.out.println(baseURL + " BaseURL");
                            System.out.println(link + " LINK");

                            boolean b = tempHash.add(link);

                            System.out.println(b + " IS ADD " + " " + deep + " LEVEL");
                        }
                    }

                    if (firstTime) {
                        //первый раз заполняем глобальный список
                        hash.addAll(tempHash);
                        firstTime = false;
                    } else {
                        //последующие разы удаляем уже известные сайты
                        tempHash.removeAll(hash);
                        //и добавляем которые будут посещены на текущем этапе
                        hash.addAll(tempHash);
                    }

                    deep++;
                    System.out.println("//--------------------------NEXT " + deep + " level" + "--------------------------------");
                    for (String x : tempHash) {
                        getHTML(x);
                    }
                    deep--;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Probably wrong URL");
            throw e;
        }
    }

    private void getImg(ListIterator<Element> le, Document doc) {
        String baseUri = doc.baseUri();
        String link = "";
        while (le.hasNext()) {
            try {
                link = le.next().attr("src"); //получаем аттрибут src

                if (!link.startsWith("http")) { //для относительных ссылок
                    link = doc.baseUri().endsWith("/") ? baseUri.substring(0, baseUri.length() - 1) + link : baseUri + link;
                    System.out.println(link);
                }
                download(link);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Something wrong with getting image");
            }
        }

    }

    private void download(String link) throws IOException {
        URL url = new URL(link);
        //---------------- соединение ---------------------
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(1000);
            urlConnection.setRequestProperty("http.agent", "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:86.0) Gecko/20100101 Firefox/86.0");
            urlConnection.setRequestMethod("GET");
            imageCurrSize = (urlConnection).getContentLengthLong();

            String output = urlConnection.getContentType() + ", " + imageCurrSize + ", " + imgNumber + ".jpeg";
            System.out.println(output);
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Can't download");
        }

        if (imageCurrSize < imageMinSize) {
            return;
        }

        //---------------- запись в файл-------------------
        try {
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(outputFolder + imgNumber + ".jpeg");
            System.out.println(imgNumber + " recorded");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            imgNumber++;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Problem with file writing");
        }
    }
}

public class Main {

    public static void main(String[] args) {
        // write your code here
        new Act();

    }

}
