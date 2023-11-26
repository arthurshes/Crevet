package workwork.test.andropediagits.presenter.lesson.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import workwork.test.andropediagits.R

import workwork.test.andropediagits.data.local.entities.levels.LevelEntity
import workwork.test.andropediagits.databinding.LessonItemBinding
import workwork.test.andropediagits.presenter.lesson.ListLessonsFragmentArgs
import workwork.test.andropediagits.presenter.lesson.ListLessonsFragmentDirections
import workwork.test.andropediagits.presenter.lesson.utils.ShowDialogHelper


class ListLessonsAdapter(
    private val args: ListLessonsFragmentArgs,
    private val context: Context
) :
    RecyclerView.Adapter<ListLessonsAdapter.LessonsHolder>() {
    var removeFavLevel: ((Int) -> Unit)? = null
    var addFavLevel: ((Int) -> Unit)? = null
    var buyThemeClick: (() -> Unit)? = null
    private var isDialogOpen = false
    inner class LessonsHolder(val binding: LessonItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonsHolder {
        return LessonsHolder(
            LessonItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return diffList.currentList.size
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: LessonsHolder, position: Int) {
        val currentLesson = diffList.currentList[position]
        holder.binding.apply {
            tvTitleLessonItem.text = currentLesson.levelName
            tvNumberLesson.text = "${context.getString(R.string.lesson_number)} ${position+1}"
            tvTopicTitle.text = args.courseName

            if (currentLesson.isFav) {
                btnFavClickeds.visibility = View.VISIBLE
                btnFav.visibility = View.GONE
            } else {
                btnFavClickeds.visibility = View.GONE
                btnFav.visibility = View.VISIBLE
            }

            btnFav.setOnClickListener {
                addFavLevel?.invoke(currentLesson.uniqueLevelId)
                btnFavClickeds.visibility = View.VISIBLE
                btnFav.visibility = View.GONE
            }

            btnFavClickeds.setOnClickListener {
                removeFavLevel?.invoke(currentLesson.uniqueLevelId)
                btnFavClickeds.visibility = View.GONE
                btnFav.visibility = View.VISIBLE
            }


            lessonCard.setOnClickListener {
                Log.d(
                    "forofjorjforjfojrofjorjfor",
                    "courseName:${args.courseName},uniqueThemeID:${args.ThemeId}"
                )


                if (position == diffList.currentList.size) {
                      if(!isDialogOpen){

                          ShowDialogHelper.showDialogAttention(context,{
                              val action =
                                  ListLessonsFragmentDirections.actionListLessonsFragmentToVictorineFragment(
                                      args.ThemeId,
                                      args.courseName,
                                      currentLesson.courseNumber,
                                      args.courseNameReal
                                  )
                              Navigation.findNavController(root).navigate(action)
                          }) {
                              isDialogOpen = false
                          }
                      }
                    isDialogOpen = true
                } else {
                    val action =
                        ListLessonsFragmentDirections.actionListLessonsFragmentToLessonFragment(
                            currentLesson.uniqueLevelId,
                            args.ThemeId,
                            args.courseName,
                            args.courseNameReal,
                            args.isTerm,
                            currentLesson.courseNumber,
                            currentLesson.themeNumber,
                            currentLesson.levelNumber
                        )
                    Navigation.findNavController(root).navigate(action)
                }
            }
        }
    }

    private val diffCall = object : DiffUtil.ItemCallback<LevelEntity>() {
        override fun areItemsTheSame(oldItem: LevelEntity, newItem: LevelEntity): Boolean {
            return oldItem.uniqueLevelId == newItem.uniqueLevelId
        }

        override fun areContentsTheSame(oldItem: LevelEntity, newItem: LevelEntity): Boolean {
            return oldItem == newItem
        }
    }

    val diffList = AsyncListDiffer(this, diffCall)
}