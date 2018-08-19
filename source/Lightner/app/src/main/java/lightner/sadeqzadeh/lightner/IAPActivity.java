package lightner.sadeqzadeh.lightner;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import lightner.sadeqzadeh.lightner.fragment.DownloadPackageFragment;
import lightner.sadeqzadeh.lightner.util.IabHelper;
import lightner.sadeqzadeh.lightner.util.IabResult;
import lightner.sadeqzadeh.lightner.util.Purchase;

public class IAPActivity extends AppCompatActivity {

    private static final String TAG = IAPActivity.class.getName();
    IabHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String base64EncodedPublicKey=  "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwC3jgRLYRj+0xcjee9urNoEuRo72pWKQk2gd36hdkDLYxBicuIwGWzV9hfmmSu/36llEf4wHpZt44iS7PfZouD9tbL1i2oorVg9O+FkNR/OeJVn0nRajT+gbY2nURDdsh4pZe+qvb0+70//nbD3YrZUhlDa7HhUvokbJqu4UmGnaNF0TUU+EtmtkrpwSt6xu2Buv+nQvcUkT2+RMkpW+TXSUodEVyMISghtWo2JwbMCAwEAAQ==";
        mHelper = new IabHelper(getApplicationContext(), base64EncodedPublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
                }

                IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
                        = new IabHelper.OnIabPurchaseFinishedListener() {
                    public void onIabPurchaseFinished(IabResult result, Purchase purchase)
                    {
                        if (result.isFailure()) {
                            Log.d(TAG, "Error purchasing: " + result);
                            Toast.makeText(IAPActivity.this.getApplicationContext(),getString(R.string.error_in_purchase), Toast.LENGTH_LONG).show();
                            return;
                        }
                        //String sku = purchase.getSku();

                    }
                };
                mHelper.launchPurchaseFlow(IAPActivity.this,"1",1,mPurchaseFinishedListener);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

}
