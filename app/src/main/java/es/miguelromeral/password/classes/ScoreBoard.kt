package es.miguelromeral.password.classes

import android.content.res.Resources
import es.miguelromeral.password.R
import es.miguelromeral.password.classes.options.Levels

class ScoreBoard {

    companion object {
        const val SCORE_HIT = 200
        const val SCORE_MISS = -100

        const val SCORE_MAX_TIME_VALUE = 50
        const val SCORE_MAX_TIME = 30000L

        const val SCORE_MIN_TIME_VALUE = 2000
        const val SCORE_MIN_TIME = 5000L

        const val SCORE_BONUS_MEDIUM = 0.25
        const val SCORE_BONUS_HARD = 0.5


        fun getScore(resources: Resources, time: Long = 0L, solved: Boolean = false, failed: Boolean = false, level: String = Levels.DEFAULT_LEVEL): Int =
                        if(solved){
                            SCORE_HIT + getBonus(resources, time, level)
                        }else if(failed){
                            SCORE_MISS
                        }else{
                            0
                        }


        private fun getBonus(resources: Resources, time: Long, level: String): Int {

            var points = if (time > SCORE_MAX_TIME)
                SCORE_MAX_TIME_VALUE
            else if (time < SCORE_MIN_TIME)
                SCORE_MIN_TIME_VALUE
            else {
                val maxtime = SCORE_MAX_TIME - SCORE_MIN_TIME
                val midtime = time - SCORE_MIN_TIME
                val maxscore = SCORE_MIN_TIME_VALUE - SCORE_MAX_TIME_VALUE
                val pct = (midtime.toDouble() / maxtime.toDouble())
                val part = pct * maxscore
                (maxscore - part).toInt()
            }



            val bonus = when(level){
                resources.getString(R.string.value_level_medium) -> (points * SCORE_BONUS_MEDIUM).toInt()
                resources.getString(R.string.value_level_hard) -> (points * SCORE_BONUS_HARD).toInt()
                else -> 0
            }

            return points + bonus
        }

    }
}