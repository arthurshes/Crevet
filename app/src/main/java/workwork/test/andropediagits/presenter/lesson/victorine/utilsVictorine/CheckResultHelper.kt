package workwork.test.andropediagits.presenter.lesson.victorine.utilsVictorine



object CheckResultHelper {
  /*  fun checkTryAgainResult(
        resultTest: (ErrorStateView) -> Unit,
        mistakeTest: (String) -> Unit,
        viewModel: VictorineViewModel,
        args: VictorineFragmentArgs,
        isNeedTryAgain: Boolean,
        context: Context
    ) {
        var resultTryAgain: ErrorEnum? = null
        viewModel.tryAgainSendProgress({ resultTryAgain = it })
        when (resultTryAgain) {
            ErrorEnum.SUCCESS -> {
                viewModel.checkTestResult(
                    args.uniqueThemeId,
                    { resultTest(it) },
                    { mistakeTest(it) })
            }

            ErrorEnum.OFFLINETHEMEBUY -> {
                viewModel.checkTestResult(
                    args.uniqueThemeId,
                    { resultTest(it) },
                    { mistakeTest(it) })
            }

            ErrorEnum.OFFLINEMODE -> {
                viewModel.checkTestResult(
                    args.uniqueThemeId,
                    { resultTest(it) },
                    { mistakeTest(it) })
            }

            ErrorEnum.TIMEOUTERROR -> {
                ShowDialogHelper.showDialogTimeOutError(context , false) {
                    isNeedTryAgain = it
                }
                if (isNeedTryAgain) {
                    checkTryAgainResult(
                        resultTest,
                        mistakeTest,
                        viewModel,
                        args,
                        isNeedTryAgain,
                        context
                    )
                }
            }

            ErrorEnum.NULLPOINTERROR -> {
                ShowDialogHelper.showDialogUnknownError(context, false) {
                    isNeedTryAgain = it
                }
                if (isNeedTryAgain) {
                    checkTryAgainResult(
                        resultTest,
                        mistakeTest,
                        viewModel,
                        args,
                        isNeedTryAgain,
                        requireContext()
                    )
                }
            }

            ErrorEnum.NOTNETWORK -> {
                ShowDialogHelper.showDialogNotNetworkError(
                    context,
                    false
                ) { isNeedTryAgain = it }
                if (isNeedTryAgain) {
                    checkTryAgainResult(
                        resultTest,
                        mistakeTest,
                        viewModel,
                        args,
                        isNeedTryAgain,
                        requireContext()
                    )
                }
            }

            ErrorEnum.ERROR -> {
                ShowDialogHelper.showDialogUnknownError(context, false) {
                    isNeedTryAgain = it
                }
                if (isNeedTryAgain) {
                    checkTryAgainResult(
                        resultTest,
                        mistakeTest,
                        viewModel,
                        args,
                        isNeedTryAgain,
                        requireContext()
                    )
                }
            }

            ErrorEnum.UNKNOWNERROR -> {
                ShowDialogHelper.showDialogUnknownError(context, false) {
                    isNeedTryAgain = it
                }
                if (isNeedTryAgain) {
                    checkTryAgainResult(
                        resultTest,
                        mistakeTest,
                        viewModel,
                        args,
                        isNeedTryAgain,
                        requireContext()
                    )
                }
            }

            else -> {
                ShowDialogHelper.showDialogUnknownError(context, false) {
                    isNeedTryAgain = it
                }
                if (isNeedTryAgain) {
                    checkTryAgainResult(
                        resultTest,
                        mistakeTest,
                        viewModel,
                        args,
                        isNeedTryAgain,
                        requireContext()
                    )
                }
            }
        }
    }*/
}