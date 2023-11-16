package workwork.test.andropediagits.domain.googbilling

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.SkuDetailsParams
import com.android.billingclient.api.SkuDetailsResult
import com.android.billingclient.api.consumePurchase


import com.google.common.collect.ImmutableList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import workwork.test.andropediagits.core.utils.Constatns.COURSE_BUY_FREE_ID
import workwork.test.andropediagits.core.utils.Constatns.COURSE_BUY_ID
import workwork.test.andropediagits.core.utils.Constatns.FIFTY_ANDROPOINT_BUY_ID
import workwork.test.andropediagits.core.utils.Constatns.FIVEHUNDRED_ANDROPOINT_BUY
import workwork.test.andropediagits.core.utils.Constatns.HUNDRED_ANDROPOINT_BUY
import workwork.test.andropediagits.core.utils.Constatns.INFINITY_ANDANDROPOINT_BUY
import workwork.test.andropediagits.core.utils.Constatns.ONE_ANDROPOINT_BUY_ID
import workwork.test.andropediagits.core.utils.Constatns.PREMIUM_MONTH_BUY
import workwork.test.andropediagits.core.utils.Constatns.PREMIUM_SIX_MONTH_BUY
import workwork.test.andropediagits.core.utils.Constatns.PREMIUM_YEAR_BUY
import workwork.test.andropediagits.core.utils.Constatns.TEN_ANDROPOINT_BUY_ID
import workwork.test.andropediagits.core.utils.Constatns.THEME_BUY_CALCUL
import workwork.test.andropediagits.core.utils.Constatns.THEME_BUY_NEWS_LIST
import workwork.test.andropediagits.core.utils.Constatns.THEME_BUY_NOTEST
import javax.inject.Inject


class BillingManager(val context: AppCompatActivity) {


  var courseBuyWithNumber:((Int)->Unit)?=null
  var theneBuyWithUniqueId:((Int)->Unit)?=null
  var addAndropointsCount:((Int)->Unit)?=null
    var infinityAndropoints:(()->Unit)?=null
    var premiumBuyWithTerm:((Int)->Unit)?=null

    private var billingClient: BillingClient?=null
    private var whatProdBuy:PayState?=null
    private var purchase: Purchase?=null
    private var courseBuyNumber:Int?=null
    private var uniqueThemeIdBuy:Int?=null



    private fun queryProduct(productId: String) {
        val queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
            .setProductList(
                ImmutableList.of(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(productId)
                        .setProductType(
                            BillingClient.ProductType.INAPP
                        )
                        .build()
                )
            )
            .build()

        billingClient?.queryProductDetailsAsync(
            queryProductDetailsParams
        ) { billingResult, productDetailsList ->
            if (productDetailsList.isNotEmpty()) {
                productDetailsList?.get(0)?.let {
                    makePurchase(it)
                }
                productDetailsList?.get(1)?.let {
                    makePurchase(it)
                }
                productDetailsList?.get(2)?.let {
                    makePurchase(it)
                }
                productDetailsList?.get(3)?.let {
                    makePurchase(it)
                }
                productDetailsList?.get(4)?.let {
                    makePurchase(it)
                }
                productDetailsList?.get(5)?.let {
                    makePurchase(it)
                }
                productDetailsList?.get(6)?.let {
                    makePurchase(it)
                }
                productDetailsList?.get(7)?.let {
                    makePurchase(it)
                }
                productDetailsList?.get(8)?.let {
                    makePurchase(it)
                }
                productDetailsList?.get(9)?.let {
                    makePurchase(it)
                }
                productDetailsList?.get(10)?.let {
                    makePurchase(it)
                }
                productDetailsList?.get(11)?.let {
                    makePurchase(it)
                }
                productDetailsList?.get(12)?.let {
                    makePurchase(it)
                }
                productDetailsList?.get(13)?.let {
                    makePurchase(it)
                }
            } else {
                Log.i("TAG", "onProductDetailsResponse: No products")
            }
        }
    }

     fun billingSetup(whatBuy: PayState,courseNumber:Int?=null,uniqueThemeID:Int?=null) {
        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()



        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(
                billingResult: BillingResult
            ) {
                if (billingResult.responseCode ==
                    BillingClient.BillingResponseCode.OK
                ) {
                    courseNumber?.let {
                        courseBuyNumber = it
                    }
                    uniqueThemeID?.let {
                        uniqueThemeIdBuy = it
                    }
                    whatProdBuy = whatBuy
                    when(whatBuy){
                        PayState.COURSEBUYADVANCED -> {
                            queryProduct(COURSE_BUY_FREE_ID)
                        }
                        PayState.COURSEBUYINDEPTH -> {
                            queryProduct(COURSE_BUY_ID)
                        }

                        PayState.ONEANDROPOINTBUY -> {
                            queryProduct(ONE_ANDROPOINT_BUY_ID)
                        }
                        PayState.TENANDROPOINTBUY -> {
                            queryProduct(TEN_ANDROPOINT_BUY_ID)
                        }
                        PayState.FIFTYANDROPOINTBUY -> {
                            queryProduct(FIFTY_ANDROPOINT_BUY_ID)
                        }
                        PayState.HUNDREDANDROPOINTBUY -> {
                            queryProduct(HUNDRED_ANDROPOINT_BUY)
                        }
                        PayState.FIVEHUNDREDANDROPOINTBUY -> {
                            queryProduct(FIVEHUNDRED_ANDROPOINT_BUY)
                        }
                        PayState.THOUSANDANDROPOINTBUY -> {
                            queryProduct(INFINITY_ANDANDROPOINT_BUY)
                        }
                        PayState.PREMIUMONEMOUNTHBUY -> {
                            queryProduct(PREMIUM_MONTH_BUY)
                        }
                        PayState.PREMIUMSIXMOUNTHBUY -> {
                            queryProduct(PREMIUM_SIX_MONTH_BUY)
                        }
                        PayState.PREMIUMYEARBUY -> {
                            queryProduct(PREMIUM_YEAR_BUY)
                        }

                        PayState.THEMEBUYCALCUL -> {
                            queryProduct(THEME_BUY_CALCUL)
                        }
                        PayState.THEMEBUYNEWSLIST -> {
                            queryProduct(THEME_BUY_NEWS_LIST)
                        }
                        PayState.THEMEBUYNOTES -> {
                            queryProduct(THEME_BUY_NOTEST)
                        }
                    }
                    Log.i("TAG", "OnBillingSetupFinish connected")

                } else {
                    Log.i("TAG", "OnBillingSetupFinish failed")
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.i("TAG", "OnBillingSetupFinish connection lost")
            }
        })
    }

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode ==
                BillingClient.BillingResponseCode.OK
                && purchases != null
            ) {
                for (purchase in purchases) {
                    completePurchase(purchase)
                }
            } else if (billingResult.responseCode ==
                BillingClient.BillingResponseCode.USER_CANCELED
            ) {
                Log.i("TAG", "onPurchasesUpdated: Purchase Canceled")
            } else {
                Log.i("TAG", "onPurchasesUpdated: Error")
            }
        }

    private fun completePurchase(item: Purchase) {
        purchase = item
        if (purchase?.purchaseState == Purchase.PurchaseState.PURCHASED) {

            when(whatProdBuy){
                PayState.COURSEBUYADVANCED -> {
                    courseBuyNumber?.let {
                        courseBuyWithNumber?.invoke(it)
                    }
                }
                PayState.COURSEBUYINDEPTH -> {
                    courseBuyNumber?.let {
                        courseBuyWithNumber?.invoke(it)
                    }
                }

                PayState.ONEANDROPOINTBUY -> {
                    consumePurchase(item)
                    addAndropointsCount?.invoke(1)
                }
                PayState.TENANDROPOINTBUY -> {
                    consumePurchase(item)
                    addAndropointsCount?.invoke(10)
                }
                PayState.FIFTYANDROPOINTBUY -> {
                    consumePurchase(item)
                    addAndropointsCount?.invoke(50)
                }
                PayState.HUNDREDANDROPOINTBUY -> {
                    consumePurchase(item)
                    addAndropointsCount?.invoke(100)
                }
                PayState.FIVEHUNDREDANDROPOINTBUY -> {
                    consumePurchase(item)
                    addAndropointsCount?.invoke(500)
                }
                PayState.THOUSANDANDROPOINTBUY -> {
                    infinityAndropoints?.invoke()
                }
                PayState.PREMIUMONEMOUNTHBUY -> {
                    consumePurchase(item)
                    premiumBuyWithTerm?.invoke(1)

                }
                PayState.PREMIUMSIXMOUNTHBUY -> {
                    consumePurchase(item)
                    premiumBuyWithTerm?.invoke(6)

                }
                PayState.PREMIUMYEARBUY -> {
                    consumePurchase(item)
                    premiumBuyWithTerm?.invoke(12)

                }
                null -> {
                    TODO()
                }

                PayState.THEMEBUYCALCUL -> {
                   theneBuyWithUniqueId?.invoke(uniqueThemeIdBuy ?: 0)
                }
                PayState.THEMEBUYNEWSLIST -> {
                    theneBuyWithUniqueId?.invoke(uniqueThemeIdBuy ?: 0)
                }
                PayState.THEMEBUYNOTES -> {
                    theneBuyWithUniqueId?.invoke(uniqueThemeIdBuy ?: 0)
                }
            }
        }
    }

    private fun makePurchase(productDetails: ProductDetails) {
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                ImmutableList.of(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .build()
                )
            )
            .build()

        billingClient?.launchBillingFlow(context, billingFlowParams)
    }

    private fun consumePurchase(purchase: Purchase) {

        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            val result = billingClient?.consumePurchase(consumeParams)
            if (result?.billingResult?.responseCode ==
                BillingClient.BillingResponseCode.OK) {
//                runOnUiThread() {
//                    Toast.makeText(this@MainActivity,"Покупка успешна потрачена",Toast.LENGTH_LONG).show()
//
//                }
            }
        }
    }





}