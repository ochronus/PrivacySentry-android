package com.ochronus.privacysentry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {


    ListView mListView;
    private ArrayAdapter arrayAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        
        mListView = (ListView) findViewById(R.id.list_view);

        PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        ArrayList<String> appnames = new ArrayList<String>();
        for (ApplicationInfo applicationInfo : packages) {
           //Log.d("test", "App: " + applicationInfo.name + " Package: " + applicationInfo.packageName);

           try {
              PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS);

              //Get Permissions
              String[] requestedPermissions = packageInfo.requestedPermissions;

              if(requestedPermissions != null) {
                 for (int i = 0; i < requestedPermissions.length; i++) {
                    //Log.d("test", requestedPermissions[i]);
                 }
              }
           } catch (NameNotFoundException e) {
              e.printStackTrace();
           }
        }
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(intent, PackageManager.GET_META_DATA);
        Log.d("ize", String.valueOf(apps.size()));
        for (ResolveInfo app : apps) {
        	try {
				PackageInfo pi = getPackageManager().getPackageInfo(app.activityInfo.packageName, PackageManager.GET_PERMISSIONS);
				String[] requestedPermissions = pi.requestedPermissions;
				StringBuilder sb = new StringBuilder();
				for (String perm : requestedPermissions) {
					sb.append(perm.split("\\.")[2] + ",");
				}
				Log.d("INFO", app.loadLabel(pm).toString() + " " + sb.toString());
        	} catch (NameNotFoundException e) {
        		Log.d("OOPS", e.getMessage());
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}

        	//Log.d("alma", app.activityInfo.name + " " + app.loadLabel(pm));
        	appnames.add(app.loadLabel(pm).toString());
        	
        }
        try {
	        String [] stringArray = appnames.toArray(new String[appnames.size()]);
	        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, appnames);
	        for (String appname: appnames) {
	        	//Log.i("APPNAME", appname);
	        }
	        mListView.setAdapter(arrayAdapter);
        }
	    catch (Exception e) {
	    	Log.i("ERROR", e.getMessage());
	    	e.printStackTrace();
	    }
        

    }
    
    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
                : false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    


   

}
