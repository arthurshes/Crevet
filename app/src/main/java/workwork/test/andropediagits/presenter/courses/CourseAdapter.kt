package workwork.test.andropediagits.presenter.courses

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import workwork.test.andropediagits.R
import workwork.test.andropediagits.data.local.entities.course.CourseEntity
import workwork.test.andropediagits.databinding.CoursItemBinding

class CourseAdapter(private val context: Context) :
    RecyclerView.Adapter<CourseAdapter.CourseHolder>() {
    var onClickCourse: ((Int) -> Unit)? = null
    var onCourseName: ((String) -> Unit)? = null
    var onPossibleBuy: ((Boolean) -> Unit)? = null
    var isCourseOpen: ((Boolean) -> Unit)? = null
    var andropointPrice: ((Int) -> Unit)? = null
    var buyCourseNumber: ((Int) -> Unit)? = null
    var rubPrice: ((Int) -> Unit)? = null

    inner class CourseHolder(val binding: CoursItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseHolder {
        return CourseHolder(
            CoursItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return diffList.currentList.size
    }

    override fun onBindViewHolder(holder: CourseHolder, position: Int) {
        val currentCourse = diffList.currentList[position]
        holder.binding.apply {
            tvTitle.text = currentCourse.courseName
            tvDescription.text = currentCourse.description
            cardCourse.setOnClickListener {
                if (!currentCourse.isOpen) {
                    andropointPrice?.invoke(currentCourse.coursePriceAndropoint ?: 0)
                    rubPrice?.invoke(currentCourse.coursePriceRub ?: 0)
                    buyCourseNumber?.invoke(currentCourse.courseNumber)
                } else {
                    onClickCourse?.invoke(currentCourse.courseNumber)
                    onCourseName?.invoke(currentCourse.courseName)
                    onPossibleBuy?.invoke(currentCourse.possibleToOpenCourseFree)
                    isCourseOpen?.invoke(currentCourse.isOpen)
                }
            }
            val currentTheme = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            if (!currentCourse.isOpen) {
                if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
                    btnLock.visibility = View.VISIBLE
                    cardCourse.background =
                        ContextCompat.getDrawable(context, R.drawable.custom_card_course_premium_black)
                }else{
                    btnLock.visibility = View.VISIBLE
                    cardCourse.background =
                        ContextCompat.getDrawable(context, R.drawable.custom_card_course_premium)
                }

            }
        }

    }

    private val diffCall = object : DiffUtil.ItemCallback<CourseEntity>() {
        override fun areItemsTheSame(oldItem: CourseEntity, newItem: CourseEntity): Boolean {
            return oldItem.courseNumber == newItem.courseNumber
        }

        override fun areContentsTheSame(oldItem: CourseEntity, newItem: CourseEntity): Boolean {
            return oldItem == newItem
        }
    }
    val diffList = AsyncListDiffer(this, diffCall)
}

