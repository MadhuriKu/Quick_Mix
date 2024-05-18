package com.orpat.quickmix.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.kalpesh.krecyclerviewadapter.KRecyclerViewAdapter
import com.kalpesh.krecyclerviewadapter.KRecyclerViewHolder
import com.kalpesh.krecyclerviewadapter.KRecyclerViewHolderCallBack
import com.kalpesh.krecyclerviewadapter.KRecyclerViewItemClickListener
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.orpat.quickmix.R
import com.orpat.quickmix.adapter.TimeSLotViewHolder
import com.orpat.quickmix.api.APIClient
import com.orpat.quickmix.model.TimeSlotData
import com.orpat.quickmix.model.TimeSlotDataClass
import com.orpat.quickmix.utility.Utils
import kotlinx.android.synthetic.main.activity_time_slot.*
import kotlinx.android.synthetic.main.calendar_day_layout.view.*
import kotlinx.android.synthetic.main.calendar_day_legend.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

class TimeSlotActivity : AppCompatActivity() {

    val timeSlotList = ArrayList<TimeSlotData>()
    private var timeAdapter: KRecyclerViewAdapter? = null

    private val today = LocalDate.now()
    var selectedDate:LocalDate = LocalDate.now()
    private var selectedTimeSlotID:Number? = null
    var selectedTimeSlot:String? = null
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    val displayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_slot)

        setupTimeSlotRV()
        getTimeSlotData()
        setupCalendar()

        submit_date_time_btn.setOnClickListener {

            val intent = Intent()
            intent.putExtra("slot_id", selectedTimeSlotID.toString())
            intent.putExtra("date", selectedDate.toString())
            intent.putExtra("selected_time_slot", selectedTimeSlot)
            setResult(Activity.RESULT_OK, intent)
            finish()

        }
    }

    private fun setupTimeSlotRV() {

        time_slot_rv!!.layoutManager = GridLayoutManager(this, 2)
        timeAdapter = KRecyclerViewAdapter(this, timeSlotList, object : KRecyclerViewHolderCallBack {
            override fun onCreateViewHolder(viewGroup: ViewGroup): KRecyclerViewHolder {
                val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.time_slot_menu_item, viewGroup, false)
                return TimeSLotViewHolder(itemView)
            }

            override fun onHolderDisplayed(kRecyclerViewHolder: KRecyclerViewHolder, i: Int) {}
        }, KRecyclerViewItemClickListener { kRecyclerViewHolder: KRecyclerViewHolder?, o: Any?, i: Int ->

            selectedTimeSlotID = timeSlotList[i].id
            selectedTimeSlot = timeSlotList[i].startTime + "-" + timeSlotList[i].endTime
        })
        time_slot_rv!!.adapter = timeAdapter
        timeAdapter!!.allowsSingleSelection = true
        timeAdapter!!.notifyDataSetChanged()
    }

    private fun getTimeSlotData() {
        APIClient.ApiAuthInterface().getDemoTimeSlots(selectedDate.format(formatter))
                .enqueue(object : Callback<TimeSlotDataClass> {
                    override fun onResponse(
                            call: Call<TimeSlotDataClass>,
                            response: Response<TimeSlotDataClass>
                    ) {
                        if (response.isSuccessful) {
                            updateCalendar(response.body()!!.data)

                        } else {
                            Utils.showSnackbar(response.message(), timeslotdrawer)
                        }
                    }

                    override fun onFailure(call: Call<TimeSlotDataClass>, t: Throwable) {
                        Utils.showSnackbar("Something went wrong!", timeslotdrawer)

                    }
                })
    }

    private fun updateCalendar(data: List<TimeSlotData>) {

        timeSlotList.clear()
        timeSlotList.addAll(data)
        timeAdapter!!.notifyDataSetChanged()
    }

    private fun setupCalendar() {

        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = DayOfWeek.SUNDAY

        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)
        calendarView.scrollToMonth(currentMonth)

        println("DEFAULT :" + Locale.getDefault() + " firstDayOfWeek " + firstDayOfWeek)

        var selectedDate = LocalDate.now()

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val textView = view.calendarDayText

            init {

                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                         selectDate(day.date)
                    }
                }
            }

            fun selectDate(date: LocalDate) {
                selectedDate = date
                if(date.isBefore(today)){
                    timeSlotList.clear()
                    time_slot_day_selected_tv.text = "Select Valid Date"
                    timeAdapter!!.notifyDataSetChanged()
                }else{
                    getTimeSlotData()
                    calendarView.notifyCalendarChanged()
                    time_slot_day_selected_tv.text = "Date selected : "+selectedDate.format(displayFormatter)

                }

            }
        }

        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()
                textView.alpha = if (day.owner == DayOwner.THIS_MONTH && day.date.isBefore(today)) 0.3f else 1f

                if (day.owner == DayOwner.THIS_MONTH) {
                    when (day.date) {
                        today -> {
                            textView.setTextColor(Color.WHITE)
                            textView.setBackgroundResource(R.drawable.ic_explore_button)
                        }

                        selectedDate -> {
                            selectedDate = day.date
                            textView.setTextColor(Color.WHITE)
                            textView.setBackgroundResource(R.drawable.ic_explore_button)

                        }

                        else -> {
                            textView.setTextColor(Color.GRAY)
                            textView.background = null
                        }
                    }
                    textView.visibility = View.VISIBLE

                } else {
                    textView.visibility = View.INVISIBLE
                }
            }
        }
        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = view.exHeaderMonthText
        }
        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                @SuppressLint("SetTextI18n") // Concatenation warning for `setText` call.
                container.textView.text = "${month.yearMonth.month.name.toLowerCase().capitalize()} ${month.year}"
            }
        }
    }

    }