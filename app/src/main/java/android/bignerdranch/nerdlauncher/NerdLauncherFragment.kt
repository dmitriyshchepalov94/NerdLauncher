package android.bignerdranch.nerdlauncher

import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_nerd_launcher.*
import kotlinx.android.synthetic.main.simple_item.*
import java.util.*
import kotlin.Comparator

class NerdLauncherFragment: Fragment() {

    companion object
    {
        const val TAG = "NerdLauncherFragment"
        fun newInstance():NerdLauncherFragment
        {
            return NerdLauncherFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nerd_launcher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
    }

    private fun setupAdapter()
    {
        val startupIntent = Intent(Intent.ACTION_MAIN)
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pm = activity?.packageManager
        val activities = pm?.queryIntentActivities(startupIntent, 0)

        Collections.sort(activities,
            object : Comparator<ResolveInfo>
            {
                override fun compare(p0: ResolveInfo?, p1: ResolveInfo?): Int {
                    val pm = activity?.packageManager
                    return String.CASE_INSENSITIVE_ORDER.compare(p0?.loadLabel(pm).toString(), p1?.loadLabel(pm).toString())
                }

            }
        )

        app_recycler_view.apply {
            adapter = activities?.let { ActivityAdapter(it) }
            layoutManager = LinearLayoutManager(activity)
        }

        Log.i(TAG, "Found ${activities?.size} activities")
    }


    inner class ActivityHolder(val view: View): RecyclerView.ViewHolder(view), View.OnClickListener
    {
        private var mResolveInfo: ResolveInfo? = null
        private val tv = view.findViewById<TextView>(R.id.app_text_view)
        private val iv = view.findViewById<ImageView>(R.id.icon_image)
        init {
            tv.setOnClickListener(this)
        }

        fun bindActivity(resolveInfo: ResolveInfo)
        {
            mResolveInfo = resolveInfo

            val pm = activity?.packageManager
            val appName = mResolveInfo?.loadLabel(pm).toString()

            tv.text = appName
            iv.setImageDrawable(mResolveInfo!!.loadIcon(pm))

        }

        override fun onClick(p0: View?) {
            val activityInfo = mResolveInfo?.activityInfo

            val i = Intent(Intent.ACTION_MAIN).setClassName(activityInfo?.applicationInfo!!.packageName, activityInfo?.name!!).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
        }
    }

    inner class ActivityAdapter(val activities: List<ResolveInfo>): RecyclerView.Adapter<ActivityHolder>()
    {
        override fun getItemCount(): Int {
            return activities.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityHolder {
           return ActivityHolder(LayoutInflater.from(activity).inflate(R.layout.simple_item, parent, false))
        }

        override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
            val act = activities.get(position)
            holder.bindActivity(act)

        }
    }
}