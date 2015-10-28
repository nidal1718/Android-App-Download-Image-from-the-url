//Courtesy : http://wptrafficanalyzer.in/blog/android-http-access-with-httpurlconnection-to-download-image-example/

package com.example.nidalahammed.downloadimagetaskv2;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  /** Getting a reference to the button  in the layout activity_main.xml
        Button btnDownload = (Button) findViewById(R.id.button);

       // /** Defining a click event listener for the button */
        OnClickListener downloadListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                 //   String path = "http://www.maplecityrubber.com/wp-content/themes/maple_city/images/balloon_red.png" ;

                 //   /** Getting a reference to Edit text containing url */
                    EditText etUrl = (EditText) findViewById(R.id.urlText);
                    // etUrl.setText(path);

                   // /** Creating a new non-ui thread task */
                    DownloadTask downloadTask = new DownloadTask();

             //       /** Starting the task created above */
                   downloadTask.execute(etUrl.getText().toString());

                }else{
                    Toast.makeText(getBaseContext(), "Network is not Available", Toast.LENGTH_SHORT).show();
                }
            }

        };

      //  /** Setting Click listener for the download button */
        btnDownload.setOnClickListener(downloadListener);
    }


    public void onClear(View v)
    {  EditText urllink = (EditText) findViewById(R.id.urlText);
        urllink.setText("");

        ImageView iView = (ImageView) findViewById(R.id.image);
        iView.setImageDrawable(null);
        Toast.makeText(getBaseContext(), "Cleared", Toast.LENGTH_SHORT).show();

    }
    private boolean isNetworkAvailable(){
        boolean available = false;
      //  /** Getting the system's connectivity service */
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

     //   /** Getting active network interface  to get the network's status */
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo !=null && networkInfo.isAvailable())
            available = true;

     //   /** Returning the status of the network */
        return available;
    }

    private Bitmap downloadUrl(String strUrl) throws IOException{
        Bitmap bitmap=null;
        InputStream iStream = null;
        try{
            URL url = new URL(strUrl);
       //     /** Creating an http connection to communicate with url */
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

       //     /** Connecting to url */
            urlConnection.connect();

       //     /** Reading data from url */
            iStream = urlConnection.getInputStream();

      //      /** Creating a bitmap from the stream returned from the url */
            bitmap = BitmapFactory.decodeStream(iStream);

        }catch(Exception e){
            Log.d("Exception occurred", e.toString());
        }finally{
            iStream.close();
        }
        return bitmap;
    }

    private class DownloadTask extends AsyncTask<String, Integer, Bitmap>{
        Bitmap bitmap = null;
        @Override
        protected Bitmap doInBackground(String... url) {
            try{
                bitmap = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
      //      /** Getting a reference to ImageView to display the* downloaded image

            ImageView iView = (ImageView) findViewById(R.id.image);

      //      /** Displaying the downloaded image */
            iView.setImageBitmap(result);



        //    /** Showing a message, on completion of download process */
           // Toast.makeText(getBaseContext(), "Image downloaded successfully", Toast.LENGTH_SHORT).show();
        }
    }

}