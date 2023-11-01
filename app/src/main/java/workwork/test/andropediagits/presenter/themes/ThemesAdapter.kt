package workwork.test.andropediagits.presenter.themes

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView



import workwork.test.andropediagits.data.local.entities.theme.ThemeEntity
import workwork.test.andropediagits.databinding.ThemeItemBinding

class ThemesAdapter(val args:ThemesFragmentArgs) : RecyclerView.Adapter<ThemesAdapter.ThemeHolder>() {

    inner class ThemeHolder(val binding: ThemeItemBinding):RecyclerView.ViewHolder(binding.root)

    private var isNavigate = false
    var currentThemePassed:((Boolean)->Unit)?=null
    var checkThisThemeTerm:((Int)->Unit)?=null
    var currentThemeName:((String)->Unit)?=null
     var removeFavorite:((Int)->Unit)?=null
    var addFavorite:((Int)->Unit)?=null
//     var checkClickThemeUniqueId:((Int)->Unit)?=null
//    var checkClickThemePassed:((Boolean)->Unit)?=null
////themeClose
    var themeCloseUniqueThemeId:((Int)->Unit)?=null
    var themeClosePossible:((Boolean)->Unit)?=null
    ///checkTermTheme
    var checkTermThemeUniqueThemeId:((Int)->Unit)?=null
    var buyThemeUniqueId:((Int)->Unit)?=null
    var buyThemePrice:((Int)->Unit)?=null
    var buyThemePossible:((Boolean)->Unit)?=null
    var buyThemeAndropointPrice:((Int)->Unit)?=null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeHolder {
        return ThemeHolder(ThemeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = diffList.currentList.size

    override fun onBindViewHolder(holder: ThemeHolder, position: Int) {
        val currentTheme = diffList.currentList[position]


        holder.binding.apply {
//                 if(!buy){
//                    cardCloseTheme.visibility= View.VISIBLE
//                }
//                   if(!currentTheme.isOpen){
//                       cardCloseTheme.visibility = View.VISIBLE
//                   }

            if(!currentTheme.isOpen&&currentTheme.themePrice==null){
                btnFav.visibility = View.GONE
                cardCloseTheme.visibility = View.VISIBLE
            } else if(currentTheme.themePrice!=null){
                btnFav.visibility = View.GONE
                cardBuyAndropointsOrMoney.visibility = View.VISIBLE
            }

            if(currentTheme.isFav){
                cardCloseTheme.visibility = View.GONE
                btnFavClicked.visibility = View.VISIBLE
                btnFav.visibility = View.GONE
            } else{
                btnFavClicked.visibility = View.GONE
                btnFav.visibility = View.VISIBLE
            }

            btnFav.setOnClickListener {
                if(currentTheme.isOpen){
                    Log.d("themeTffrfrgt","addClicklistId:${currentTheme.uniqueThemeId}")
                    addFavorite?.invoke(currentTheme.uniqueThemeId)
                        btnFavClicked.visibility = View.VISIBLE
                        btnFav.visibility = View.GONE

                }
            }

            btnFavClicked.setOnClickListener {
                Log.d("themeTffrfrgt","removeClicklistId:${currentTheme.uniqueThemeId}")
                removeFavorite?.invoke(currentTheme.uniqueThemeId)
                btnFavClicked.visibility = View.GONE
                btnFav.visibility = View.VISIBLE

            }
            tvTitleTheme.text = currentTheme.themeName
            themeCard.setOnClickListener {
                if(!currentTheme.isOpen&&currentTheme.themePrice!=null){
                    buyThemeUniqueId?.invoke(currentTheme.uniqueThemeId)
                    buyThemePrice?.invoke(currentTheme.themePrice)
                    buyThemePossible?.invoke(currentTheme.possibleToOpenThemeFree)
                    buyThemeAndropointPrice?.invoke(currentTheme.andropointsPrice ?: 0)
                }
                     if(currentTheme.isOpen){

                         Log.d("themeicjjir","uniqueThemeID:${currentTheme.uniqueThemeId},themeName:${currentTheme.themeName}")
                         if(!isNavigate){
                             isNavigate = true
                             val action = ThemesFragmentDirections.actionThemesFragmentToListLessonsFragment(currentTheme.uniqueThemeId, currentTheme.themeName, courseNameReal = args.courseName, isThemePassed = currentTheme.isThemePassed)
                             root.let { Navigation.findNavController(it).navigate(action) }
                         }

                     } else if(currentTheme.termDateApi!=null&&currentTheme.termDateApi!=""){
                         checkThisThemeTerm?.invoke(currentTheme.uniqueThemeId)
                         currentThemeName?.invoke(currentTheme.themeName)
                         currentThemePassed?.invoke(currentTheme.isThemePassed)
                         //*//222
                     }
//                    if (!buy) {
//                        checkClickThemeUniqueId?.invoke(currentTheme.uniqueThemeId)
//                        checkClickThemePossible?.invoke(currentTheme.possibleToOpenThemeFree)
//                    } else {
//                        if (!currentTheme.isOpen) {
//                            themeCloseUniqueThemeId?.invoke(currentTheme.uniqueThemeId)
//                            themeClosePossible?.invoke(currentTheme.possibleToOpenThemeFree)
//                        }else{
//                            checkTermThemeUniqueThemeId?.invoke(currentTheme.uniqueThemeId)
//                        }
//                    }
                }

            }
        }

    private val diffCall = object : DiffUtil.ItemCallback<ThemeEntity>(){
        override fun areItemsTheSame(oldItem: ThemeEntity, newItem: ThemeEntity): Boolean {
            return oldItem.uniqueThemeId == newItem.uniqueThemeId
        }

        override fun areContentsTheSame(oldItem: ThemeEntity, newItem: ThemeEntity): Boolean {
            return oldItem == newItem
        }

    }

    val diffList = AsyncListDiffer(this,diffCall)


    }



//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeHolder {
//        val viewBinding = ThemeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ThemeHolder(viewBinding,buy)
//    }
//
//    override fun onBindViewHolder(holder: ThemeHolder, position: Int) {
//        holder.setData(themeArray[position])
//    }
//
//    override fun getItemCount(): Int {
//        return themeArray.size
//    }
//
//    class ThemeHolder(
//        private val binding: ThemeItemBinding,
//        private val isBuy: Boolean
//    ) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun setData(theme: ThemeEntity) {
//            binding.apply {
//                if(!isBuy){
//                    cardCloseTheme.visibility= View.VISIBLE
//                }
//
//                   if(!theme.isOpen){
//                       cardCloseTheme.visibility = View.VISIBLE
//                   }
//
//                tvTitleTheme.text = theme.themeName
//                tvNumberOfCompletedLevelsItemTheme.text = theme.lessonsCount.toString()
//                tvNumberTheme.text = theme.themeNumber.toString()
//               // imTheme.setImageBitmap(theme.imageTheme)
//
//                themeCard.setOnClickListener {
//
//                }
//                itemView.setOnClickListener {
//                    if (!isBuy) {
//                        themeListener.checkThemeBuy(
//                            theme.uniqueThemeId,
//                            theme.possibleToOpenThemeFree
//                        )
//                    } else {
//
//                        if (!theme.isOpen) {
//                            themeCard.setOnClickListener {
//                                themeListener.themeClose(theme.uniqueThemeId, theme.possibleToOpenThemeFree)
//                            }
//                        }else{
//                            themeListener.checkTermTheme(theme.uniqueThemeId)
//                        }
//                    }
//                }
//               isFav(theme)
//            }
//        }
//        private fun isFav(theme: ThemeEntity)= with(binding){
//            if (theme.isFav){
//                btnFav.setImageResource(R.drawable.ic_bookmark)
//            }else{
//                btnFav.setImageResource(R.drawable.ic_bookmark_not_pressed)
//            }
//        }
//    }
//
//    fun updateAdapter(newList: List<ThemeEntity>) {
//        val adsListTemp = ArrayList<ThemeEntity>()
//        adsListTemp.addAll(themeArray)
//        adsListTemp.addAll(newList)
//        val difResult = DiffUtil.calculateDiff(DiffUtilHelper(themeArray, adsListTemp))
//        difResult.dispatchUpdatesTo(this)
//        themeArray.clear()
//        themeArray.addAll(/*adsListTemp*/newList)
//    }

//interface ThemeListener{
//    fun checkTermTheme(uniqueThemeId: Int)
//    fun checkThemeBuy(uniqueThemeId: Int, possibleToOpenThemeFree: Boolean)
//    fun themeClose(uniqueThemeId: Int, possibleToOpenThemeFree: Boolean)
//}

//class DiffUtilHelper(
//    private val oldList: List<ThemeEntity>,
//    private val newList: List<ThemeEntity>
//) : DiffUtil.Callback() {
//    override fun getOldListSize(): Int {
//        return oldList.size
//    }
//
//    override fun getNewListSize(): Int {
//        return newList.size
//    }
//
//    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        return oldList[oldItemPosition].uniqueThemeId == newList[newItemPosition].uniqueThemeId
//    }
//
//    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        return oldList[oldItemPosition] == newList[newItemPosition]
//    }
//}


