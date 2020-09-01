package android.bignerdranch.nerdlauncher

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

abstract class SingleFragmentActivity: AppCompatActivity() {

    @LayoutRes
    protected open fun getLayoutResId(): Int
    {
        return R.layout.activity_fragment
    }

    abstract fun createFragment(): Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        val fm: FragmentManager = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)
        if(fragment == null)
        {
            fragment = createFragment()
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit()
        }
    }

}