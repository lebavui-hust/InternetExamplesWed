package com.example.internetexamples;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);

        findViewById(R.id.btn_quick_work).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("Quick work ... done");
            }
        });

        findViewById(R.id.btn_slow_work).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SlowTask().execute(15);
            }
        });

        findViewById(R.id.btn_download_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadTask().execute();
            }
        });

        // new GetTask().execute();
        // new PostTask().execute();

//        ImageView imageView = findViewById(R.id.image_view);
//        Picasso.get().load("https://lebavui.github.io/walls/wall1.jpg").into(imageView);

//        try {
//            String jsonString = "{\"name\":\"Peter\", \"age\":20, \"gender\":\"male\",\"address\":{\"country\":\"Vietnam\", \"city\":\"Hanoi\"}}";
//            JSONObject jObj = new JSONObject(jsonString);
//            String name = jObj.getString("name");
//            int age = jObj.getInt("age");
//            String gender = jObj.getString("gender");
//            String country = jObj.getJSONObject("address").getString("country");
//            Log.v("TAG", name + " - " + age + " - " + gender + " - " + country);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

//        try {
//            String jsonString = "[{\"name\":\"Peter\",\"age\":20,\"gender\":\"male\"},{\"name\":\"Alice\",\"age\":21,\"gender\":\"female\"},{\"name\":\"Bob\",\"age\":22,\"gender\":\"male\"}]";
//            JSONArray jArr = new JSONArray(jsonString);
//            int count = jArr.length();
//            Log.v("TAG", "Count: " + count);
//
//            for (int i = 0; i < jArr.length(); i++) {
//                JSONObject jObj = jArr.getJSONObject(i);
//                String name = jObj.getString("name");
//                int age = jObj.getInt("age");
//                String gender = jObj.getString("gender");
//                Log.v("TAG", name + " - " + age + " - " + gender);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

        List<StudentModel> students = new ArrayList<>();
        students.add(new StudentModel("20171234", "Nguyen Van A", "Hanoi"));
        students.add(new StudentModel("20172345", "Tran Van B", "Danang"));
        students.add(new StudentModel("20174567", "Le Van C", "Hochiminh"));

        try {
            JSONArray jArr = new JSONArray();
            for (int i = 0; i < students.size(); i++) {
                StudentModel student = students.get(i);
                JSONObject jObj = new JSONObject();
                jObj.put("mssv", student.getMssv());
                jObj.put("hoten", student.getHoten());
                jObj.put("address", student.getAddress());
                jArr.put(jObj);
            }

            String jsonString = jArr.toString();
            Log.v("TAG", jsonString);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class SlowTask extends AsyncTask<Integer, Integer, Boolean> {


        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            textView.setText("Slow work ... started");
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Processing");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            int total = params[0];

            try {
                for (int i = 0; i < total; i++) {
                    Thread.sleep(1000);
                    publishProgress(i + 1, total);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            int total = values[1];

            int percent = progress * 100 / total;
            textView.setText("Slow work ... " + progress +"s " + percent + "%");

            dialog.setMax(total);
            dialog.setProgress(progress);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            dialog.dismiss();

            if (result)
                textView.setText("Slow work ... done");
            else
                textView.setText("Slow work ... failed");
        }
    }

    private class GetTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                URL url = new URL("https://httpbin.org/get?p1=value1&p2=value2");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                int responseCode = con.getResponseCode();
                Log.v("TAG", "Response code: " + responseCode);

                InputStream is = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String data = "";
                String line;

                while ((line = reader.readLine()) != null)
                    data += line + "\n";

                reader.close();
                is.close();

                Log.v("TAG", data);

                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

    private class DownloadTask extends AsyncTask<Void, Integer, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Downloading");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                URL url = new URL("https://lebavui.github.io/videos/ecard.mp4");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                int responseCode = con.getResponseCode();
                Log.v("TAG", "Response code: " + responseCode);

                InputStream is = con.getInputStream();

                String filePath = Environment.getExternalStorageDirectory() + "/test2.mp4";
                File file = new File(filePath);
                OutputStream os = new FileOutputStream(file);

                // OutputStream os = openFileOutput("test1.mp4", MODE_PRIVATE);

                int total = con.getContentLength();

                byte[] buffer = new byte[1024];
                int len, progress = 0;

                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                    progress += len;
                    publishProgress(progress, total);
                }

                os.close();
                is.close();

                Log.v("TAG", "Download ... done");

                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            int total = values[1];

            dialog.setMax(total);
            dialog.setProgress(progress);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.dismiss();
        }
    }

    private class PostTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                URL url = new URL("https://httpbin.org/post");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");

                String params = "param1=value1&param2=value2";

                con.setDoOutput(true);
                DataOutputStream writer = new DataOutputStream(con.getOutputStream());
                writer.writeBytes(params);
                writer.close();

                int responseCode = con.getResponseCode();
                Log.v("TAG", "Response code: " + responseCode);

                InputStream is = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String data = "";
                String line;

                while ((line = reader.readLine()) != null)
                    data += line + "\n";

                reader.close();
                is.close();

                Log.v("TAG", data);

                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }
}