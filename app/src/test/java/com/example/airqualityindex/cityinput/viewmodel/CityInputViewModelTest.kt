package com.example.airqualityindex.cityinput.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import com.example.airqualityindex.aqidisplay.model.toAQIDisplayData
import com.example.airqualityindex.aqidisplay.view.NavAQIDisplay
import com.example.airqualityindex.cityinput.model.AQIData
import com.example.airqualityindex.cityinput.model.CityData
import com.example.airqualityindex.cityinput.model.Daily
import com.example.airqualityindex.cityinput.model.Forecast
import com.example.airqualityindex.cityinput.model.Pollutant
import com.example.airqualityindex.cityinput.model.service.AQIApiService
import com.example.airqualityindex.cityinput.model.service.AQIResult
import com.example.airqualityindex.location.LocationHandler
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import retrofit2.Response
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class CityInputViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiService: AQIApiService

    @Mock
    private lateinit var locationHandler: LocationHandler

    @Mock
    private lateinit var navController: NavController

    private lateinit var viewModel: CityInputViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        
        MockitoAnnotations.openMocks(this)

        Dispatchers.setMain(testDispatcher)

        viewModel = CityInputViewModel(apiService, locationHandler, navController)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testFetchAQIByCitySuccess() = runTest {
        val cityName = "Philadelphia"
        val mockResponse: Response<AQIResult> = Response.success(AQIResult.Success(mockAQIDataSuccessful()))

        `when`(apiService.getAQIByCity(cityName)).thenReturn(mockResponse)

        viewModel.fetchAQI(cityName)
        advanceUntilIdle()

        assertEquals(CityInputState.Loading, viewModel.state.first())
        verify(navController).navigate(NavAQIDisplay(mockAQIDataSuccessful().toAQIDisplayData().toJson()))
    }

    @Test
    fun testFetchAQIByLocationError() = runTest {
        val cityName = "Unknown City"
        val mockErrorResponse: Response<AQIResult> = Response.success(AQIResult.Error("City not found"))

        `when`(apiService.getAQIByCity(cityName)).thenReturn(mockErrorResponse)

        viewModel.fetchAQI(cityName)
        advanceUntilIdle()

        assertEquals(
            CityInputState.Error(CityInputErrorType.TOAST, message = "City not found"),
            viewModel.state.first()
        )
    }

    private fun mockAQIDataSuccessful() = AQIData(
        15,
        CityData("Philadelphia", listOf(37.48293,-122.20348)),
        Forecast(
            Daily(
                pm25 = listOf(Pollutant(25, LocalDate.now())),
                pm10 = listOf(Pollutant(25, LocalDate.now()))
            )
        )
    )

}