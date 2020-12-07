package com.cat.music.ui.music.video

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cat.music.R
import com.cat.music.ui.music.mv.VideoListAdapter

class YouTubeDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.motion_youtube)
        val motionLayout = findViewById<MotionLayout>(R.id.motionLayout).apply {
            savedInstanceState
        }
        findViewById<RecyclerView>(R.id.recyclerview_front).apply {
            adapter = VideoListAdapter(mutableListOf())
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(this@YouTubeDemoActivity)
        }
        val debugMode = if (intent.getBooleanExtra("showPaths", false)) {
            MotionLayout.DEBUG_SHOW_PATH
        } else {
            MotionLayout.DEBUG_SHOW_NONE
        }
        motionLayout.setDebugMode(debugMode)
    }
}