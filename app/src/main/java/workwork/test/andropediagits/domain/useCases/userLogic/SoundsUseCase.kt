package workwork.test.andropediagits.domain.useCases.userLogic


import android.content.Context
import android.media.MediaPlayer
import workwork.test.andropediagits.R
import workwork.test.andropediagits.domain.useCases.userLogic.state.SoundState

class SoundsUseCase {

    private var player:MediaPlayer?=null



    fun soundsFun(soundState: SoundState,context: Context){

        when(soundState){
            SoundState.THEMEOPENSOUND -> {
                TODO()
            }
            SoundState.THEMETERMCLOSE -> {
                soundPlay(R.raw.test_fail_sound,context)
            }
            SoundState.INTERACTIVETOUCHSOUND -> {
                TODO()
            }
            SoundState.INTERACTIVENEXTSOUND -> {
                TODO()
            }
            SoundState.VICTROINETOUCHSOUND -> {
                TODO()
            }
            SoundState.VICTORINENEXTSOUND -> {
                TODO()
            }
            SoundState.TERMNOTEND -> {
                soundPlay(R.raw.zvuk_chasov,context)
            }
            SoundState.COURSEBUYSOUND -> {
                TODO()
            }
            SoundState.STRIKEMODESOUND -> {
                soundPlay(R.raw.strike_mode_sound,context)
            }
        }
    }

    private fun soundPlay(songId:Int,context: Context){
            if(player==null){
              player = MediaPlayer.create(context,songId)
              player?.start()
          }
    }

    fun playerStop(){
        player?.reset()
        player?.release()
        player = null
    }

}