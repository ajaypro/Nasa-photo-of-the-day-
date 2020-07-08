package com.deepak.nasa.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deepak.nasa.data.network.Endpoints.TYPE_IMAGE
import com.deepak.nasa.data.network.Endpoints.TYPE_VIDEO
import com.deepak.nasa.data.network.Resource
import com.deepak.nasa.data.network.Resource.*
import com.deepak.nasa.data.network.response.Apod
import com.deepak.nasa.data.repository.ApodRepository
import com.deepak.nasa.utils.common.defaultDate
import com.deepak.nasa.utils.rx.SchedulerProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.io.IOException
import java.text.DecimalFormat
import java.util.*

class CommonViewModel(private val apodRepository: ApodRepository,
                      private val compositeDisposable: CompositeDisposable,
                      private val schedulerProvider: SchedulerProvider): ViewModel() {


    private val selectedDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    private val currentDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    private val decimalFormat: DecimalFormat by lazy { DecimalFormat("00") }

    // Livedata that gives url of image or video
    private val _dataUrl : MutableLiveData<String> = MutableLiveData()
    val dataUrl : LiveData<String> get() = _dataUrl

    // livedata for mediatype
    private val _mediaType : MutableLiveData<String> = MutableLiveData()
    val mediaType : MutableLiveData<String> get() = _mediaType

    // livedata for photo of the day
    private val _apodData: MutableLiveData<Resource<Apod>> = MutableLiveData()
    val apodData: LiveData<Resource<Apod>> get() = _apodData

    private val defaultDate: String = defaultDate(decimalFormat, selectedDateCalendar)

    private val _selectedDate: MutableLiveData<String> =
        MutableLiveData(defaultDate) // date in yyyy-mm-dd


    init {
        getApod()
    }

    private fun getApod() {
        _apodData.value = Loading
        compositeDisposable.add(apodRepository.getDefaultApod()
                .subscribeOn(schedulerProvider.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    getMedia(it)
                }, {
                    getErrorMessage(it)
                })
        )
    }


    fun getApodForDate() {
        _apodData.value = Loading
        compositeDisposable.add(
            apodRepository.getApod(_selectedDate.value)
                .subscribeOn(schedulerProvider.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    getMedia(it)
                }, {
                    getErrorMessage(it)
                })
        )
    }

    private fun getMedia(apod: Apod?){
        if (apod != null) {
            _apodData.value = Success(apod)
            when (apod.mediaType) {
                TYPE_IMAGE -> {
                    _mediaType.value = TYPE_IMAGE
                    _dataUrl.value = apod.hdurl
                }
                else -> {
                    _mediaType.value = TYPE_VIDEO
                    _dataUrl.value = apod.url
                }
            }
        } else {
            _apodData.value = Error("Error fetching data!")
        }
    }

    private fun getErrorMessage(throwable: Throwable){

        if (throwable is IOException) {
            _apodData.value = NetworkError
        } else {
            _apodData.value = Error("${throwable.message}")
        }
    }

    fun setDate(year : Int, month : Int, day : Int) {
        with(selectedDateCalendar) {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
            if (time == currentDateCalendar.time) {
                getApod()
            } else {
                _selectedDate.value = "${decimalFormat.format(year)}-${decimalFormat.format(
                    month + 1
                )}-${decimalFormat.format(day)}"
                getApodForDate()
            }
        }
    }

    fun getDate() : Calendar = selectedDateCalendar

    fun reloadVideo() {
        this._dataUrl.value = _dataUrl.value
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }


}