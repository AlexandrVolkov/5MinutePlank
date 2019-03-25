package com.example.a5minuteplank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.os.CountDownTimer
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var exerciseDurationInSec: Long = 0

    val sequence: IntArray  = intArrayOf(0,1,2,2,3,3,0,1)
    val timingProportion: IntArray = intArrayOf(0,2,3,4,5,6,7,8)
    val activities: IntArray = intArrayOf(0,1,2,3)
    var currentActivity: Int = 0
    var currentActivityIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val countDownInterval: Long = 1000
        startButton.setOnClickListener {
            exerciseDurationInSec = exerciseDuration.text.toString().toLong()
            currentActivity = 0
            currentActivityIndex = 0
            timer(exerciseDurationInSec * 1000, countDownInterval).start()
            it.isEnabled = false
        }
    }

    private fun timer(millisInFuture:Long, countDownInterval:Long):CountDownTimer{
        return object: CountDownTimer(millisInFuture,countDownInterval){
            override fun onTick(millisUntilFinished: Long){
                val timeRemainingInSec: Long = millisUntilFinished / 1000
                exerciseDuration.setText(timeRemainingInSec.toString())
                val calculatedActivityIndex: Int = calculateCurrentActivityIndex(timeRemainingInSec)
                val calculatedActivity: Int = sequence[calculatedActivityIndex]
                if (currentActivityIndex != calculatedActivityIndex && calculatedActivity == currentActivity) {
                    currentActivityIndex = calculatedActivityIndex
                    infoText.setText("Switch side")
                }
                if (calculatedActivity != currentActivity) {
                    infoText.setText("")
                    currentActivity = calculatedActivity
                    switchActivity()
                }
                if (currentActivityIndex != calculatedActivityIndex) {
                    currentActivityIndex = calculatedActivityIndex
                }
            }

            override fun onFinish() {
                startButton.isEnabled = true
                exerciseDuration.setText(exerciseDurationInSec.toString())
                showActivity(0);
            }
        }
    }

    private fun switchActivity(){
        showActivity(currentActivity)
    }

    private fun calculateCurrentActivityIndex(timeRemainingInSec: Long): Int{
        val weight: Long = exerciseDurationInSec/10
        val currentProportionNumber: Long = (exerciseDurationInSec - timeRemainingInSec)/weight

        var savedIndex = 0
        for((index: Int, value: Int) in timingProportion.withIndex()) {
            if (currentProportionNumber >= value) {
                savedIndex = index
            }
        }
        return savedIndex
    }

    private fun showActivity(activityId: Int){
        disableAllActivities()
        if (activityId == 0) {
            fullPlank.visibility = View.VISIBLE
        }
        if (activityId == 1) {
            elbowPlank.visibility = View.VISIBLE
        }
        if (activityId == 2) {
            raisedLegPlank.visibility = View.VISIBLE
        }
        if (activityId == 3) {
            sidePlank.visibility = View.VISIBLE
        }
    }

    private fun disableAllActivities(){
        sidePlank.visibility = View.GONE
        elbowPlank.visibility = View.GONE
        fullPlank.visibility = View.GONE
        raisedLegPlank.visibility = View.GONE
    }
}
