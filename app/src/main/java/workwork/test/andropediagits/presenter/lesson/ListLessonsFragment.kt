package workwork.test.andropediagits.presenter.lesson

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import workwork.test.andropediagits.R
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.databinding.FragmentListLessonsBinding
import workwork.test.andropediagits.domain.useCases.userLogic.state.BuyForAndropointStates
import workwork.test.andropediagits.presenter.bottomSheet.VictorineBottomSheetFragment
import workwork.test.andropediagits.presenter.lesson.adapters.ListLessonsAdapter
import workwork.test.andropediagits.presenter.lesson.utils.ShowDialogHelper
import workwork.test.andropediagits.presenter.lesson.viewmodel.ListLessonsViewModel

@AndroidEntryPoint
class ListLessonsFragment : Fragment() {
    private val args: ListLessonsFragmentArgs by navArgs()
    private var binding: FragmentListLessonsBinding? = null
    private val viewModel: ListLessonsViewModel by viewModels()
    private var adapter: ListLessonsAdapter?=null
    private var isFavoriteLessons = false
    private var isTermVar = false
    private var isThemePassed = false
    private var terString = "Invalid date"
    private var bs: VictorineBottomSheetFragment?=null
    private var timer: CountDownTimer?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("thisScreen","ListLessonsFragment")
        binding = FragmentListLessonsBinding.inflate(inflater, container, false)
        bs = VictorineBottomSheetFragment()
        viewModel.thisThemePassed({
            Log.d("gprkgrkgorkgprkgPassed3333","bla bla kekeke: "+it.toString())
            if(it){
                isThemePassed = true
                binding?.textViewTestingngtb?.text = getString(R.string.testing_passed)
                binding?.linearVictorineTests?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.theme_passed))
            }else{
                isThemePassed = false
                binding?.linearVictorineTests?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.error))

            }
        },args.ThemeId)
        viewModel.checkVictorineExistTheme({
            if(it){
                binding?.btnInVictorine?.isClickable = true
                binding?.btnInVictorine?.visibility = View.VISIBLE
            }else{
                binding?.btnInVictorine?.isClickable = false
                binding?.btnInVictorine?.visibility = View.GONE
            }
        },args.ThemeId)
        return binding?.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("gtgtgtg",args.courseName)
        Log.d("gtgtgtg",args.ThemeId.toString())
        adapter = ListLessonsAdapter(args,requireContext())
        adapter?.buyThemeClick = {
            buyThemeTreatmentResult()
        }

        adapter?.addFavLevel = {uniqueLessonID->
            viewModel.thisLessonFavorite(uniqueLessonID)
        }

        adapter?.removeFavLevel = {uniqueLessonID->
            if(isFavoriteLessons){
                viewModel.putUniqueThemeIdForGetLevels(args.ThemeId) { lessons ->
                    isFavoriteLessons = false
                    binding?.btnFavoritesLessons?.setImageResource(R.drawable.ic_bookmark_not_pressed_white)
                    adapter?.diffList?.submitList(lessons)
                }
                viewModel.thisLessonNotFavorite(uniqueLessonID)
            }else{
                viewModel.thisLessonNotFavorite(uniqueLessonID)
            }
        }

        binding?.apply {
            tvNameTheme.text = args.courseName
            rcViewLesson.layoutManager = LinearLayoutManager(requireContext())
            rcViewLesson.adapter = adapter

            viewModel.putUniqueThemeIdForGetLevels(args.ThemeId) {
                adapter?.diffList?.submitList(it)
            }
        }

        binding?.btnFavoritesLessons?.setOnClickListener {
            favoritesLessons()
        }

        binding?.btnInVictorine?.setOnClickListener {
            binding?.btnInVictorine?.isClickable = false
            startTimerViewAdsFun()
            checkCurrentThemeTermTreatmentResult()
        }

        binding?.btnBackFromLessons?.setOnClickListener {
            val action = ListLessonsFragmentDirections.actionListLessonsFragmentToThemesFragment(adapter?.diffList?.currentList?.get(0)?.courseNumber ?: 1, args.courseNameReal)
            binding?.root?.let { Navigation.findNavController(it).navigate(action) }
        }
    }

    private fun favoritesLessons() {
        if(!isFavoriteLessons){
            viewModel.getAllFavoriteLessons(args.ThemeId) { favLessons ->
                if (favLessons.isEmpty()) {
                    Toast.makeText(requireContext(), getString(R.string.you_not_have_favorite_lessons), Toast.LENGTH_SHORT).show()
                } else {
                    isFavoriteLessons = true
                    binding?.btnFavoritesLessons?.setImageResource(R.drawable.baseline_bookmark_24)
                    adapter?.diffList?.submitList(favLessons)
                }
            }
        } else{
            viewModel.putUniqueThemeIdForGetLevels(args.ThemeId) { lessons ->
                isFavoriteLessons = false
                binding?.btnFavoritesLessons?.setImageResource(R.drawable.ic_bookmark_not_pressed_white)
                adapter?.diffList?.submitList(lessons)
            }
        }
    }

    private fun checkCurrentThemeTermTreatmentResult() {
        viewModel.checkCurrentThemeTerm(args.ThemeId,{ state->
            Log.d("hei4399439u39439493u",state.toString())
            when(state){
                ErrorEnum.SUCCESS -> {
                    Log.d("isTernVarStateVictorine",isTermVar.toString())
                    howManyTermTreatmentResult()
                }
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                            checkCurrentThemeTermTreatmentResult()
                        }
                    }
                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            checkCurrentThemeTermTreatmentResult()
                        }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            checkCurrentThemeTermTreatmentResult()
                        }
                    }
                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                            checkCurrentThemeTermTreatmentResult()
                        }
                    }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            checkCurrentThemeTermTreatmentResult()
                        }
                    }
                }
                ErrorEnum.OFFLINEMODE ->{

                    requireActivity().runOnUiThread {
                        showDialogOffline()
                    }
                }
                ErrorEnum.OFFLINETHEMEBUY -> {

                    requireActivity().runOnUiThread {
                     showDialogOffline()
                    }
                }
            }
        }, { isTerm ->
            isTermVar = isTerm
        })
    }

    private fun showDialogOffline(){
        binding?.dimViewListLesson?.visibility = View.VISIBLE
        ShowDialogHelper.showDialogOffline(requireContext(),{
            val data = Bundle()
            Log.d("isTernVarStateVictoriwwww22222ne",args.ThemeId.toString())
            Log.d("isTernVarStateVictoriwwww22222ne",args.courseName.toString())
            Log.d("isTernVarStateVictoriwwww22222ne",args.courseNameReal.toString())
            Log.d("isTernVarStateVictoriwwww22222ne",isThemePassed.toString())
            Log.d("isTernVarStateVictoriwwww22222ne",adapter?.diffList?.currentList?.get(0)?.courseNumber .toString())
            Log.d("isTernVarStateVictoriwwww22222ne",isTermVar.toString())

            data.putInt("uniqueThemeID",args.ThemeId)
            data.putString("courseName",args.courseName)
            data.putString("courseNameReal",args.courseNameReal)
            data.putBoolean("isThemePassed",isThemePassed)
            data.putInt("courseNumber",adapter?.diffList?.currentList?.get(0)?.courseNumber ?: 1)
            data.putString("termDate","Invalid date" )
            data.putBoolean("isTerm",isTermVar)
            bs?.arguments = data
            bs?.show(requireActivity().supportFragmentManager, "Tag2")
        },{
            binding?.dimViewListLesson?.visibility = View.GONE
        })
    }

    private fun howManyTermTreatmentResult() {
        viewModel.howManyTerm({stateHow->
            when(stateHow){
                ErrorEnum.SUCCESS -> {
                    requireActivity().runOnUiThread {
                        val data = Bundle()
                        data.putInt("uniqueThemeID",args.ThemeId)
                        data.putString("courseName",args.courseName)
                        data.putString("courseNameReal",args.courseNameReal)
                        data.putBoolean("isThemePassed",isThemePassed)
                        data.putInt("courseNumber",adapter?.diffList?.currentList?.get(0)?.courseNumber ?: 1)
                        data.putString("termDate",terString )
                        data.putBoolean("isTerm",isTermVar)
                        bs?.arguments = data
                        bs?.show(requireActivity().supportFragmentManager, "Tag2")
                    }

                }
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                            howManyTermTreatmentResult()
                        }
                    }
                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            howManyTermTreatmentResult()
                        }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            howManyTermTreatmentResult()
                        }
                    }
                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                            howManyTermTreatmentResult()
                        }
                    }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            howManyTermTreatmentResult()
                        }
                    }
                }
                ErrorEnum.OFFLINEMODE ->{

                    requireActivity().runOnUiThread {
                        binding?.dimViewListLesson?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewListLesson?.visibility = View.GONE
                        })
                    }
                }
                ErrorEnum.OFFLINETHEMEBUY -> {

                    requireActivity().runOnUiThread {
                        binding?.dimViewListLesson?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewListLesson?.visibility = View.GONE
                        })
                    }
                }
            }
        },{termText->
            terString = termText
        }, adapter?.diffList?.currentList?.get(0)?.themeNumber ?: 1,adapter?.diffList?.currentList?.get(0)?.courseNumber ?: 1 )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("state_key_lesson_list", viewModel.currentState)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
    @SuppressLint("SuspiciousIndentation")
    private fun buyThemeTreatmentResult() {

        var isHaveMoneyResult: BuyForAndropointStates? = null

        viewModel.buyTheme({ resultCourseBuy ->
            when (resultCourseBuy) {
                ErrorEnum.SUCCESS -> {
                    if (isHaveMoneyResult == BuyForAndropointStates.YESMONEY) {
                        requireActivity().runOnUiThread {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.theme_buy_success),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        //показ окна покупки
                    }
                }

                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                            buyThemeTreatmentResult()
                        }
                    }
                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                            buyThemeTreatmentResult()
                        }
                    }
                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            buyThemeTreatmentResult()
                        }
                    }
                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            buyThemeTreatmentResult()
                        }
                    }
                }

                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            buyThemeTreatmentResult()
                        }
                    }
                }

                else -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            buyThemeTreatmentResult()
                        }
                    }
                }
            }
        }, { isHaveMoneyResult = it }, 9990)
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==22234&&resultCode==Activity.RESULT_OK){
            val nextNavData = data?.extras
            if(nextNavData!=null){
                val value = nextNavData.getString("HowNavNext")
                if(value=="victorine"){
                    val action =
                        ListLessonsFragmentDirections.actionListLessonsFragmentToVictorineFragment(
                            args.ThemeId,
                            args.courseName,
                            adapter?.diffList?.currentList?.get(0)?.courseNumber ?: 1,
                            args.courseNameReal
                        )
                    binding?.root?.let {
                        Navigation.findNavController(it).navigate(action)
                    }
                }
            }
        }
    }

    private fun startTimerViewAdsFun(){
        timer = object : CountDownTimer(2 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) { millisUntilFinished / 1000        }
            override fun onFinish() {
                binding?.btnInVictorine?.isClickable = true
            }
        }
        timer?.start()
    }


    override fun onStop() {
        super.onStop()
        timer?.cancel()
    }
}