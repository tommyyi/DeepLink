package will.com.github.deeplink;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    
    WebView web_main;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        StetherHelper.initStetho(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        web_main = (WebView)findViewById(R.id.web_main);
        web_main.loadUrl("file:///android_asset/h5.html");
    }

    private void initWebView()
    {
        web_main.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {

                if (url.startsWith("will://"))
                {
                    Uri uri = Uri.parse(url);
                    Log.e("---------scheme: ",
                        uri.getScheme() + "host: " + uri.getHost() + "Id: " + uri.getPathSegments().get(0));
                    Toast.makeText(MainActivity.this, "打开新的页面", Toast.LENGTH_LONG).show();
                    return true; //返回true，代表要拦截这个url
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    public void onClick(View view)
    {
        Uri uri = Uri.parse("yjf://home/ids000?param1=98&param2=100");
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }
}
