package com.example.mobileapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mobileapp.databaseHelper.OnEventsRetrievedListener
import android.Manifest
import android.telephony.SmsManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class events : AppCompatActivity() {
    var addEventButton: Button? = null
    var db: databaseHelper? = null
    fun deleteEvent(eventId: String, eventView: View) {
        db = databaseHelper()
        val delVal = db?.deleteEvent(eventId)
        if (delVal == true) {
            // Remove the event's view dynamically from the layout
            val parentLayout = eventView.parent as? LinearLayout
            parentLayout?.removeView(eventView)  // Remove only the specific event's view
            Toast.makeText(this@events, "Event Deleted", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this@events, "Event Deletion Unsuccessful", Toast.LENGTH_LONG).show()
        }
    }

    fun setDateAttr(dateView: TextView, eventDate: String?) {
        val dateParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        //dynamically styling date column
        dateParams.gravity = Gravity.START
        dateParams.height = 120
        dateParams.width = 200
        dateParams.setMargins(5, 5, 10, 5)
        dateView.layoutParams = dateParams
        dateView.textSize = 12f
        dateView.text = eventDate
        dateView.id = View.generateViewId()
    }

    fun setEventAttr(eventView: TextView, eventData: String?) {
        val eventParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        //Dynamically Styling event column
        eventParams.gravity = Gravity.START
        eventParams.height = 120
        eventParams.width = 500
        eventParams.setMargins(5, 5, 5, 5)
        eventView.layoutParams = eventParams
        eventView.textSize = 15f
        eventView.text = eventData
        eventView.id = View.generateViewId()
    }

    fun setDelBtnAttr(del: Button) {
        val delParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        //Dynamically Styling delete button
        delParams.gravity = Gravity.START
        delParams.height = 120
        delParams.width = 150
        delParams.setMargins(5, 5, 5, 5)
        del.layoutParams = delParams
        del.textSize = 10f
        del.text = "X"
        del.id = View.generateViewId()
        del.setBackgroundColor(Color.rgb(255, 69, 0))
        del.setTextColor(Color.WHITE)
    }

    fun setEditBtnAttr(edit: Button) {
        val editParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        //Dynamically Styling edit button
        editParams.gravity = Gravity.START
        editParams.height = 120
        editParams.width = 150
        editParams.setMargins(5, 5, 5, 5)
        edit.layoutParams = editParams
        edit.textSize = 10f
        edit.text = "EDIT"
        edit.id = View.generateViewId()
        edit.setBackgroundColor(Color.rgb(70, 130, 180))
        edit.setTextColor(Color.WHITE)
    }
    internal fun manualSortEvents(events: MutableList<Event>, sortOption: String) {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

        for (i in 0 until events.size - 1) {
            for (j in 0 until events.size - i - 1) {
                val date1 = try { dateFormat.parse(events[j].date) } catch (e: Exception) { null }
                val date2 = try { dateFormat.parse(events[j + 1].date) } catch (e: Exception) { null }

                val shouldSwap = when (sortOption) {
                    "Most Recent" -> date1 != null && date2 != null && date1.before(date2)
                    "Least Recent" -> date1 != null && date2 != null && date1.after(date2)
                    else -> false // Keep original order (Date Added)
                }

                if (shouldSwap) {
                    val temp = events[j]
                    events[j] = events[j + 1]
                    events[j + 1] = temp
                }
            }
        }
    }

    private fun sendSms(phoneNumber: String, message: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), 1)
        } else {
            try {
                val smsManager = applicationContext.getSystemService(SmsManager::class.java)
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                Toast.makeText(this, "SMS Sent", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)

        db = databaseHelper()
        val myRoot = findViewById<LinearLayout>(R.id.linear_parent) // Get layout reference
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val todayDate = dateFormat.format(Date())

        val sortSpinner: Spinner = findViewById(R.id.event_spinner)

        // Define sorting options
        val sortOptions = listOf("Date Added", "Most Recent", "Least Recent")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sortSpinner.adapter = adapter

        // Create a container for dynamic event views
        val eventContainer = LinearLayout(this)
        eventContainer.orientation = LinearLayout.VERTICAL
        myRoot.addView(eventContainer)  // Add it to the root layout

        // Add the spinner item selection listener
        sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedOption = parent.getItemAtPosition(position).toString()
                db?.getEvents(object : OnEventsRetrievedListener {
                    override fun onEventsRetrieved(events: List<Event>) {
                        if (events.isEmpty()) {
                            Log.d("EventData", "No events found.")
                            return
                        }

                        // Clear the event container to avoid duplication
                        eventContainer.removeAllViews() // Clears only event views

                        // Sort events based on the selected option
                        val sortedEvents = events.toMutableList()
                        manualSortEvents(sortedEvents, selectedOption)

                        // Re-populate the UI with sorted events
                        for (event in sortedEvents) {
                            val eventId = event.eventId
                            val eventData = event.event
                            val eventDate = event.date
                            val layoutChild = LinearLayout(this@events)

                            val dateView = TextView(this@events)
                            setDateAttr(dateView, eventDate)

                            val eventView = TextView(this@events)
                            setEventAttr(eventView, eventData)

                            val del = Button(this@events)
                            setDelBtnAttr(del)
                            del.setOnClickListener { deleteEvent(eventId, layoutChild) }

                            val edit = Button(this@events)
                            setEditBtnAttr(edit)
                            edit.setOnClickListener {
                                val editScreen = Intent(applicationContext, edit_event::class.java)
                                editScreen.putExtra("key", eventId)
                                startActivity(editScreen)
                            }

                            // Assigning views to layout

                            layoutChild.orientation = LinearLayout.HORIZONTAL
                            layoutChild.addView(dateView)
                            layoutChild.addView(eventView)
                            layoutChild.addView(edit)
                            layoutChild.addView(del)

                            eventContainer.addView(layoutChild)

                            // Optional: Send SMS for events on today's date
                            if (eventDate == todayDate) {
                                sendSms("5551234567", "Reminder: You have an event today - $eventData")
                            }
                        }
                    }
                })
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        // Initial event population (without spinner interaction)
        db?.getEvents(object : OnEventsRetrievedListener {
            override fun onEventsRetrieved(events: List<Event>) {
                if (events.isEmpty()) {
                    Log.d("EventData", "No events found.")
                    return
                }

                // Clear the event container to avoid duplication
                eventContainer.removeAllViews()

                // Iterate through the events and add them to the layout
                for (event in events) {
                    val eventId = event.eventId
                    val eventData = event.event
                    val eventDate = event.date

                    val dateView = TextView(this@events)
                    setDateAttr(dateView, eventDate)

                    val eventView = TextView(this@events)
                    setEventAttr(eventView, eventData)

                    val del = Button(this@events)
                    setDelBtnAttr(del)
                    del.setOnClickListener { deleteEvent(eventId, eventContainer) }

                    val edit = Button(this@events)
                    setEditBtnAttr(edit)
                    edit.setOnClickListener {
                        val editScreen = Intent(applicationContext, edit_event::class.java)
                        editScreen.putExtra("key", eventId)
                        startActivity(editScreen)
                    }

                    // Assigning views to layout
                    val layoutChild = LinearLayout(this@events)
                    layoutChild.orientation = LinearLayout.HORIZONTAL
                    layoutChild.addView(dateView)
                    layoutChild.addView(eventView)
                    layoutChild.addView(edit)
                    layoutChild.addView(del)

                    eventContainer.addView(layoutChild)

                    // Optional: Send SMS for events on today's date
                    if (eventDate == todayDate) {
                        sendSms("5551234567", "Reminder: You have an event today - $eventData")
                    }
                }
            }
        })

        // Add event button click listener
        addEventButton = findViewById(R.id.add_event_view)
        addEventButton?.setOnClickListener {
            val intent = Intent(applicationContext, add_event::class.java)
            startActivity(intent)
            finish()
        }
    }



}