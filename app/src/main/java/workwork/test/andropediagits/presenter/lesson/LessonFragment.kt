package workwork.test.andropediagits.presenter.lesson

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import workwork.test.andropediagits.R
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.databinding.FragmentLessonBinding
import workwork.test.andropediagits.presenter.lesson.utils.Initialization
import workwork.test.andropediagits.presenter.lesson.utils.ShowDialogHelper
import workwork.test.andropediagits.presenter.lesson.viewmodel.LessonViewModel

@AndroidEntryPoint
class LessonFragment : Fragment() {
    private var binding: FragmentLessonBinding? = null
    private val viewModel: LessonViewModel by viewModels()
    private val args: LessonFragmentArgs by navArgs()
    private var next:Int = 0
    private var isTermExistButton = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("thisScreen","LessonFragment")
        binding = FragmentLessonBinding.inflate(inflater, container, false)
        return binding?.root

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            val includedArray = arrayOf(
                includedFirst, includedSecond, includedThird, includedFourth, includedFifth,includedSixth, includedSeventh, includedEighth, includedNinth, includedTenth
            )
            includedArray.forEach { includedItem ->
                includedItem.included.btnCopyCode.setOnClickListener {
                    copyText(includedItem.included.tvCode)
                }
            }
        }
        next =  args.LevelNumber

        lifecycleScope.launch {
            viewModel.putUniqueThemeIdForGetLevels(args.uniqueThemeId)

            val tuhthughtu = viewModel.getNextContent(args.CourseNumber,args.ThemeNumber,args.LevelNumber)?.textTitle ?: "gotkgotogktogktog"
            viewModel.getNextContent(args.CourseNumber,args.ThemeNumber,args.LevelNumber, isText = {
                Log.d("firjfirjfirjfijrfjri","ttitleStart:${tuhthughtu}")
                binding!!.tvTitleLesson.text = tuhthughtu
            }, LastLesson = {
                howManyTermTreatmentResult()
            }, isVictorine = {
                viewModel.checkVictorineExistTheme({
                    if(it){
                        ShowDialogHelper.showDialogAttention(requireContext()) {
                            val action =
                                LessonFragmentDirections.actionLessonFragmentToVictorineFragment(
                                    args.uniqueThemeId,
                                    args.courseName,
                                    args.CourseNumber,
                                    args.courseNameReal
                                )
                            binding?.root?.let { it1 ->
                                Navigation.findNavController(it1).navigate(action)
                            }
                        }
                    }else{
                        Toast.makeText(requireContext(),getString(R.string.there_is_no_quiz_in_this_theme),Toast.LENGTH_LONG).show()
                        val action = LessonFragmentDirections.actionLessonFragmentToThemesFragment(args.CourseNumber, args.courseName)
                        binding?.root?.let { it1 ->
                            Navigation.findNavController(it1).navigate(action)
                        }
                    }
                },args.uniqueThemeId)
            })?.let {  Initialization.initAllViews(it,binding,requireContext())
                Log.d("LessonContentLogger",it.toString())
            }
        }
        binding!!.apply {
            btnPrevious.setOnClickListener {
                if(next>1) {
                    next--
                    lifecycleScope.launch {

                        val textTitle = viewModel.getPreviousContent(
                            args.CourseNumber,
                            args.ThemeNumber,
                            next
                        )?.textTitle
                        viewModel.getPreviousContent(args.CourseNumber, args.ThemeNumber, next)
                            ?.let { Initialization.initAllViews(it, binding, requireContext()) }
                        requireActivity().runOnUiThread {
                            if(isTermExistButton){
                                isTermExistButton = false
                                btnNext.isEnabled = true
                                btnNext.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                                binding?.btnNext?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.btnNextColor))
                                binding?.btnNext?.text = getString(R.string.next)
                            }
                            binding!!.tvTitleLesson.text = textTitle
                        }
                    }
                }

            }
            btnNext.setOnClickListener {
                Log.d("firjfirjfirjfijrfjri","nextLesson")
                next++
                Log.d("firjfirjfirjfijrfjri","nextValue${next}")
                lifecycleScope.launch {
                    val nexText =  viewModel.getNextContent(args.CourseNumber,args.ThemeNumber,next ?: 1)?.textTitle
                    Log.d("firjfirjfirjfijrfjri","courseNumberArgs:${args.CourseNumber},themeNumber:${args.ThemeNumber},levelNUmber:${next}")
                    viewModel.getNextContent(args.CourseNumber,args.ThemeNumber,next ?: 1,{
                        Log.d("firjfirjfirjfijrfjri", "nextVictorine")
                        viewModel.checkVictorineExistTheme({
                            if(it){
                                ShowDialogHelper.showDialogAttention(requireContext()) {
                                    val action =
                                        LessonFragmentDirections.actionLessonFragmentToVictorineFragment(
                                            args.uniqueThemeId,
                                            args.courseName,
                                            args.CourseNumber,
                                            args.courseNameReal
                                        )
                                    binding?.root?.let { it1 ->
                                        Navigation.findNavController(it1).navigate(action)
                                    }
                                }
                            }else{
                                Toast.makeText(requireContext(),getString(R.string.there_is_no_quiz_in_this_theme),Toast.LENGTH_LONG).show()
                                val action = LessonFragmentDirections.actionLessonFragmentToThemesFragment(args.CourseNumber, args.courseName)
                                binding?.root?.let { it1 ->
                                    Navigation.findNavController(it1).navigate(action)
                                }
                            }
                        },args.uniqueThemeId) }
                        ,{
                            requireActivity().runOnUiThread {
                                binding!!.tvTitleLesson.text = nexText
                            }
                        },{
                            howManyTermTreatmentResult()
                        }) ?.let {  Initialization.initAllViews(it,binding,requireContext()) }
                }
            }
        }
    }

    private fun howManyTermTreatmentResult() {
        var termVar = ""
        viewModel.howManyTerm({ state->
            when(state){
                ErrorEnum.SUCCESS -> {
                    if(termVar!=""){
                        isTermExistButton = true
                        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_lock) // Замените R.drawable.my_drawable на свой ресурс
                        binding?.btnNext?.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                        binding?.btnNext?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.error))
                        binding?.btnNext?.text = termVar
                        binding?.btnNext?.isEnabled = false
                    }
                }
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogNotNetworkError(requireContext()) { howManyTermTreatmentResult() }
                    }
                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {howManyTermTreatmentResult() }
                    }
                }

                ErrorEnum.NULLPOINTERROR -> {

                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {howManyTermTreatmentResult() }
                    }
                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogTimeOutError(requireContext()) {howManyTermTreatmentResult() }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) { howManyTermTreatmentResult() }
                    }
                }
                ErrorEnum.OFFLINEMODE ->{
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogOffline(requireContext())
                    }
                }
                ErrorEnum.OFFLINETHEMEBUY -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogOffline(requireContext())
                    }
                }
            }
        },{ term->
            if(term=="Invalid date"){

            }else{
                termVar = term
            }
        },args.ThemeNumber,args.CourseNumber)
    }

    private fun copyText(text: TextView){
        val textToCopy = text.text.toString()
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clipData = ClipData.newPlainText("Text Label", textToCopy)
        clipboardManager.setPrimaryClip(clipData)
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("state_key_lesson", viewModel.currentState)
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}