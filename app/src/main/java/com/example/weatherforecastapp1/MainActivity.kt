package com.example.weatherforecastapp1


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherforecastapp1.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    //https://api.openweathermap.org/data/2.5/weather?q=delhi&appid=5e0daa1804cb8609c11194b684b4b186

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData("jaipur")
        searchCity()


    }

    private fun searchCity() {
        val searchView1 = binding.searchView1
        searchView1.setOnQueryTextListener(object :androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun fetchWeatherData(cityName: String) {

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)

        val response1 = retrofit.getWeatherData(cityName,"5e0daa1804cb8609c11194b684b4b186","metric")
        response1.enqueue(object : Callback<WeatherData> {
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                val resp1 = response.body()
                if(response.isSuccessful && resp1 != null){
                    val temperature = resp1.main.temp.toString()
                    val humidity = resp1.main.humidity
                    val windSpeed = resp1.wind.speed
                    val sunRise = resp1.sys.sunrise.toLong()
                    val sunSet = resp1.sys.sunset.toLong()
                    val minTemp = resp1.main.temp_min
                    val maxTemp = resp1.main.temp_max
                    val seaLevel = resp1.main.pressure
                    val condition = resp1.weather.firstOrNull()?.main?: "unknown"


                    binding.temperatureMain.text = "$temperature ℃"
                    binding.weatherCondition.text = condition
                    binding.tvMinTemp.text = "Min Temp: $minTemp ℃"
                    binding.tvMaxTemp.text = "Max Temp: $maxTemp ℃"
                    binding.humidity1.text = "$humidity %"
                    //time called here
                    binding.sunRise1.text = "${time(sunRise)}"
                    binding.sunSet1.text = "${time(sunSet)}"
                    binding.windSpeed1.text = "$windSpeed m/s"
                    binding.seaLevel1.text = "$seaLevel hPa"

                    binding.weatherCondi2.text = condition
                    //dayName called here
                    binding.tvDay.text = dayName(System.currentTimeMillis())
                    //date called here
                    binding.tvDate.text = date()
                    binding.tv1.text = "$cityName"

                    changeImagesAccordingToWeatherCondition(condition)

                }
            }
            override fun onFailure(call: Call<WeatherData>, t: Throwable) {

            }
        })
    }

    private fun changeImagesAccordingToWeatherCondition(conditions: String) {
        when(conditions){
            "Clear Sky", "Sunny", "Clear" -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnime1.setAnimation(R.raw.sun)
            }
            "Party Clouds", "Clouds","Overcast","Mist", "Foggy" -> {
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnime1.setAnimation(R.raw.cloud)
            }
            "Love Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain" -> {
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnime1.setAnimation(R.raw.rain)
            }
            "Light Snow", "Moderate Snow", "Heavy Snow","Blizzard" -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnime1.setAnimation(R.raw.snow)
            }else -> {
            binding.root.setBackgroundResource(R.drawable.sunny_background)
            binding.lottieAnime1.setAnimation(R.raw.sun)

            }
        }
        binding.lottieAnime1.playAnimation()

    }

    private fun date(): CharSequence? {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }
    private fun time(timeStamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timeStamp*1000)))
    }

    fun dayName(timeStamp: Long): String{
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))

    }
}