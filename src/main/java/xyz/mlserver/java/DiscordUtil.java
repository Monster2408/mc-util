package xyz.mlserver.java;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DiscordUtil {

    /**
     * HTTPリクエストを送る
     * <pre>
     * 指定されたURLへ、HTTPリクエストでJSONオブジェクトを送ります。
     * </pre>
     * @param json : 送信電文（主文）
     * @param url ： 送信先URL
     **/
    public static void sendWebhook(String  json, String url){

        try{
            //送信先URLを指定してHttpコネクションを作成する
            URL sendUrl = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection)sendUrl.openConnection();

            //リクエストヘッダをセット
            con.addRequestProperty("Content-Type",
                    "application/JSON; charset=utf-8");
            con.addRequestProperty("User-Agent", "DiscordBot");
            //URLを出力利用に指示
            con.setDoOutput(true);
            //要求方法にはPOSTを指示
            con.setRequestMethod("POST");

            //要求を送信する
            // POSTデータの長さを設定
            con.setRequestProperty("Content-Length", String.valueOf(json.length()));
            //リクエストのbodyにJSON文字列を書き込む
            OutputStream stream = con.getOutputStream();
            stream.write(json.getBytes("UTF-8"));
            stream.flush();
            stream.close();

            // HTTPレスポンスコード
            final int status = con.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK
                    && status != HttpURLConnection.HTTP_NO_CONTENT) {
                //異常
                Log.debug("error:" + status);
            }

            //後始末
            con.disconnect();

        } catch(IOException e  ){
            //例外
            e.printStackTrace();
        }
    }

}
