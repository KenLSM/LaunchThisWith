package com.kenlsm.launchlinkwith

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import com.kenlsm.launchlinkwith.R.id.toolbar
import android.support.design.widget.Snackbar
import android.widget.EditText

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class PackageList(resolveInfo: ResolveInfo) {

    val resolveInfo = resolveInfo
    val title: String = resolveInfo.activityInfo.packageName
    lateinit var icon: Drawable
    lateinit var subtitle: String
}

class MainActivity : AppCompatActivity() {

    fun alert(s: String) {
        val t = Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT)
        t.show()
    }


    private lateinit var mPackageList: List<PackageList>


    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var selectionIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun handleLaunch(view: View) {
        alert("Querying")
        val queryUri = findViewById<EditText>(R.id.queryUrl).text.toString()
        val uri: Uri = Uri.parse(if (queryUri.isEmpty()) {
            "https://example.com"
        } else {
            queryUri
        })
        val intent = Intent(Intent.ACTION_VIEW, uri)

        val activities = packageManager.queryIntentActivities(
                intent,
                PackageManager.MATCH_ALL
        )
        alert("Found " + activities.size + " apps.")

        val arr: ArrayList<PackageList> = ArrayList()
        for (activity in activities) {
            val pl = PackageList(activity)
            pl.subtitle = activity.activityInfo.loadLabel(packageManager).toString()
            arr.add(pl)
            pl.icon = activity.activityInfo.loadIcon(packageManager)
        }
        mPackageList = arr
        viewManager = LinearLayoutManager(this)
        viewAdapter = MyRecycleViewAdapter(mPackageList, ::setLaunchTarget)
        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }

    fun setLaunchTarget(idx: Int, name: String) {
        alert("Selected: " + name)
        selectionIndex = idx;
    }

    fun handleSend(view: View) {
        if (selectionIndex < 0) {
            alert("No target selected yet!")
            return
        }
        val act = mPackageList[selectionIndex].resolveInfo
        intent.component = ComponentName(act.activityInfo.packageName, act.activityInfo.name)

        val launchUri = findViewById<EditText>(R.id.launchUrl).text.toString()

        if(launchUri.isEmpty()) {
            alert("No launch url yet!")
            return
        }

        intent.data = Uri.parse(launchUri)
        startActivity(intent)
    }

}


