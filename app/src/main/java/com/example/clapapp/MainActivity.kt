package com.example.clapapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var seekBar: SeekBar
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seekBar = findViewById(R.id.sbClapping)
        handler = Handler(Looper.getMainLooper())

        val play = findViewById<FloatingActionButton>(R.id.fabPlay);
        play.setOnClickListener {
            if(mediaPlayer==null){
                mediaPlayer = MediaPlayer.create(this,R.raw.applauding)
                initializeSeekBar()
            }
            mediaPlayer?.start()
        }

        val pause = findViewById<FloatingActionButton>(R.id.fabPause);
        pause.setOnClickListener {
            mediaPlayer?.pause()
        }

        val stop = findViewById<FloatingActionButton>(R.id.fabStop);
        stop.setOnClickListener {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
            handler.removeCallbacks(runnable)
            seekBar.progress = 0
        }

    }

    private fun initializeSeekBar(){
        val played = findViewById<TextView>(R.id.tvPlayed)
        val due = findViewById<TextView>(R.id.tvDue)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                val totalTime = mediaPlayer!!.duration
//                    played.text = ((totalTime*progress)/100000).toString()
                if(fromUser) mediaPlayer?.seekTo(progress)
                val playedTime = progress/1000
                if(playedTime>9)
                    played.text = "00:$playedTime"
                else
                    played.text = "00:0$playedTime"
                val dueTime = (mediaPlayer!!.duration - progress)/1000
                if(dueTime>9)
                    due.text = "00:$dueTime"
                else
                    due.text = "00:0$dueTime"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        seekBar.max = mediaPlayer!!.duration
        runnable = Runnable {
            seekBar.progress = mediaPlayer!!.currentPosition
            handler.postDelayed(runnable,1000)
        }
        handler.postDelayed(runnable,1000)

    }

}