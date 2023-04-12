package com.example.mqttkotlinsample

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnLongClickListener
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.eclipse.paho.client.mqttv3.*

/**
 * @author jbravo@uma.es (Juan Bravo-Arrabal)
 */

class ClientFragmentDcam : Fragment() {
    private lateinit var mqttClient: MQTTClient
    var REP_DELAY = 60  // milliseconds
    var mAutoIncrement = false
    var mAutoDecrement = false
    val repeatUpdateHandler = Handler()
    var counterB = 0
    var counterW = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (mqttClient.isConnected()) {
                    // Disconnect from MQTT Broker
                    mqttClient.disconnect(object : IMqttActionListener {
                        override fun onSuccess(asyncActionToken: IMqttToken?) {
                            Log.d(this.javaClass.name, "Disconnected")

                            Toast.makeText(
                                context,
                                "MQTT Disconnection success",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Disconnection success, come back to Connect Fragment
                            findNavController().navigate(R.id.action_clientFragment2_to_ConnectFragment)
                        }

                        override fun onFailure(
                            asyncActionToken: IMqttToken?,
                            exception: Throwable?
                        ) {
                            Log.d(this.javaClass.name, "Failed to disconnect")
                        }
                    })
                } else {
                    Log.d(this.javaClass.name, "Impossible to disconnect, no server connected")
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.camera_interface, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ///////////////////////////////////////////// CAM IP

        val button = view.findViewById<View>(R.id.cam_con) as Button
        val mywebView = view.findViewById<View>(R.id.vis_cam) as WebView

        // mywebView2.setWebViewClient(new WebViewClient());
        button.setOnClickListener {
            val input = view.findViewById<View>(R.id.url) as AutoCompleteTextView
            // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, COUNTRIES);
            //  textView.setAdapter(adapter);
            var url = input.text.toString()
            val separated = url.split(",").toTypedArray()
            url = separated[0] // this will contain "Fruit"
            val scale = separated[1] // this will contain " they taste good"
            mywebView.setInitialScale(scale.toInt())
            mywebView.loadUrl(url)
        }

        ///////////////////////////////////////////// MQTT

        // Get arguments passed by ConnectFragment
        val serverURI = arguments?.getString(MQTT_SERVER_URI_KEY)
        val clientId = arguments?.getString(MQTT_CLIENT_ID_KEY)
        val username = arguments?.getString(MQTT_USERNAME_KEY)
        val pwd = arguments?.getString(MQTT_PWD_KEY)

        // Check if passed arguments are valid
        if (serverURI != null &&
            clientId != null &&
            username != null &&
            pwd != null
        ) {
            // Open MQTT Broker communication
            mqttClient = MQTTClient(context, serverURI, clientId)

            // Connect and login to MQTT Broker
            mqttClient.connect(username,
                pwd,
                object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.d(this.javaClass.name, "Connection success")
/*
                        Toast.makeText(context, "MQTT Connection success", Toast.LENGTH_SHORT)
                            .show()

 */
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.d(this.javaClass.name, "Connection failure: ${exception.toString()}")

                        Toast.makeText(
                            context,
                            "MQTT Connection fails: ${exception.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Come back to Connect Fragment
                        findNavController().navigate(R.id.action_clientFragmentDcam_to_ConnectFragment)
                    }
                },
                object : MqttCallback {
                    override fun messageArrived(topic: String?, message: MqttMessage?) {
                        val msg = "Receive message: ${message.toString()} from topic: $topic"
                        Log.d(this.javaClass.name, msg)

                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }

                    override fun connectionLost(cause: Throwable?) {
                        Log.d(this.javaClass.name, "Connection lost ${cause.toString()}")
                    }

                    override fun deliveryComplete(token: IMqttDeliveryToken?) {
                        Log.d(this.javaClass.name, "Delivery complete")
                    }
                })
        } else {
            // Arguments are not valid, come back to Connect Fragment
            findNavController().navigate(R.id.action_clientFragmentDcam_to_ConnectFragment)
        }


        class RptUpdaterUp:
            Runnable {
            override fun run() {
                val textW = view.findViewById(R.id.textW) as TextView
                //textW.setText("Wrist:")
                if (mAutoDecrement) {
                    if (counterW> 0 && counterW <= 180) {
                        counterW -= 3 //incremento
                        textW.setText("Wrist:"+counterW.toString()+"º")
                    }
                    else
                        counterW = 0
                    repeatUpdateHandler.postDelayed(
                        RptUpdaterUp(),
                        REP_DELAY.toLong()
                    ) //speed of increment

                    if (mqttClient.isConnected()) {
                        val topic = "topic"
                        val message = "W" + counterW.toString() // initialized to 0
                        mqttClient.publish(topic,
                            message,
                            1,
                            false,
                            object : IMqttActionListener {
                                override fun onSuccess(asyncActionToken: IMqttToken?) {
                                    val msg = "Publish message: $message to topic: $topic"
                                    Log.d(this.javaClass.name, msg)
                                    //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }
                                override fun onFailure(
                                    asyncActionToken: IMqttToken?,
                                    exception: Throwable?
                                ) {
                                    Log.d(this.javaClass.name, "Failed to publish message to topic")
                                }
                            })
                    } else {
                        Log.d(this.javaClass.name, "Impossible to publish, no server connected")
                    }
                }
            }
        }


        view.findViewById<Button>(R.id.up_button).setOnLongClickListener(
            OnLongClickListener {

                mAutoDecrement = true // true when long-pressed button
                repeatUpdateHandler.post(RptUpdaterUp()) // incremented for REP_DELAY ms.



                false
            })

        view.findViewById<Button>(R.id.up_button)
            .setOnTouchListener(OnTouchListener { v, event ->
                if ((event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL)
                    && mAutoDecrement
                ) {
                    mAutoDecrement = false
                }
                false
            })

        // UP BUTTON
        /*
        view.findViewById<Button>(R.id.up_button).setOnClickListener {

            val topic = "topic"
            //val topic   = "pv_jba*servo"
            val message = "W0"

            if (mqttClient.isConnected()) {
                mqttClient.publish(topic,
                    message,
                    1,
                    false,
                    object : IMqttActionListener {
                        override fun onSuccess(asyncActionToken: IMqttToken?) {
                            val msg = "Publish message: $message to topic: $topic"
                            Log.d(this.javaClass.name, msg)
                            //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(
                            asyncActionToken: IMqttToken?,
                            exception: Throwable?
                        ) {
                            Log.d(this.javaClass.name, "Failed to publish message to topic")
                        }
                    })
            } else {
                Log.d(this.javaClass.name, "Impossible to publish, no server connected")
            }
        }

         */


        // DOWN BUTTON

        class RptUpdaterDown :
            Runnable {
            override fun run() {
                val textW = view.findViewById(R.id.textW) as TextView
                if (mAutoIncrement) {
                    if (counterW < 180) {
                        counterW+=3 //incremento
                        textW.setText("Wrist:" + counterW.toString() + "º")
                    }
                    else
                        counterW = 180
                    repeatUpdateHandler.postDelayed(
                        RptUpdaterDown(),
                        REP_DELAY.toLong()
                    ) //velocidad de incremento

                    if (mqttClient.isConnected()) {
                        val topic = "topic"
                        val message = "W" + counterW.toString()
                        mqttClient.publish(topic,
                            message,
                            1,
                            false,
                            object : IMqttActionListener {
                                override fun onSuccess(asyncActionToken: IMqttToken?) {
                                    val msg = "Publish message: $message to topic: $topic"
                                    Log.d(this.javaClass.name, msg)
                                    //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }
                                override fun onFailure(
                                    asyncActionToken: IMqttToken?,
                                    exception: Throwable?
                                ) {
                                    Log.d(this.javaClass.name, "Failed to publish message to topic")
                                }
                            })
                    } else {
                        Log.d(this.javaClass.name, "Impossible to publish, no server connected")
                    }
                }
            }
        }

        view.findViewById<Button>(R.id.down_button).setOnLongClickListener(
            OnLongClickListener {

                mAutoIncrement = true
                repeatUpdateHandler.post(RptUpdaterDown())

                false
            })
        // When the button is released, the incrementation of the value should be stopped.

        view.findViewById<Button>(R.id.down_button)
            .setOnTouchListener(OnTouchListener { v, event ->
                if ((event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL)
                    && mAutoIncrement
                ) {
                    mAutoIncrement = false
                }
                false
            })

       
        // LEFT BUTTON

        class RptUpdaterLeft :
            Runnable {
            override fun run() {
                val textB = view.findViewById(R.id.textB) as TextView
                //textB.setText("Base:")
                if (mAutoIncrement) {
                    if (counterB < 180) {
                        counterB+=3 //incremento
                        textB.setText("Base:"+counterB.toString()+"º")
                    }
                    else
                        counterB = 180
                    repeatUpdateHandler.postDelayed(
                        RptUpdaterLeft(),
                        REP_DELAY.toLong()
                    )

                    if (mqttClient.isConnected()) {
                        val topic = "topic"
                        val message = "B" + counterB.toString()
                        mqttClient.publish(topic,
                            message,
                            1,
                            false,
                            object : IMqttActionListener {
                                override fun onSuccess(asyncActionToken: IMqttToken?) {
                                    val msg = "Publish message: $message to topic: $topic"
                                    Log.d(this.javaClass.name, msg)
                                    //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }
                                override fun onFailure(
                                    asyncActionToken: IMqttToken?,
                                    exception: Throwable?
                                ) {
                                    Log.d(this.javaClass.name, "Failed to publish message to topic")
                                }
                            })
                    } else {
                        Log.d(this.javaClass.name, "Impossible to publish, no server connected")
                    }
                }
            }
        }

        view.findViewById<Button>(R.id.left_button).setOnLongClickListener(
            OnLongClickListener {

                mAutoIncrement = true
                repeatUpdateHandler.post(RptUpdaterLeft())

                /*
                Toast.makeText(
                    context,
                    counter.toString(),
                    Toast.LENGTH_SHORT
                ).show()

                 */

                false
            })


        view.findViewById<Button>(R.id.left_button)
            .setOnTouchListener(OnTouchListener { v, event ->
                if ((event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL)
                    && mAutoIncrement
                ) {
                    mAutoIncrement = false
                }
                false
            })




        class RptUpdaterRigth:
            Runnable { 
            override fun run() {
                if (mAutoDecrement) {
                    val textB = view.findViewById(R.id.textB) as TextView
                   // textB.setText("Base:")
                    if (counterB> 0 && counterB <= 180)
                    {
                        counterB-=3 
                        textB.setText("Base:"+counterB.toString()+"º")
                    }
                    else
                        counterB = 0
                    repeatUpdateHandler.postDelayed(
                        RptUpdaterRigth(),
                        REP_DELAY.toLong()
                    )

                    if (mqttClient.isConnected()) {
                        val topic = "topic"
                        val message = "B" + counterB.toString()
                        mqttClient.publish(topic,
                            message,
                            1,
                            false,
                            object : IMqttActionListener {
                                override fun onSuccess(asyncActionToken: IMqttToken?) {
                                    val msg = "Publish message: $message to topic: $topic"
                                    Log.d(this.javaClass.name, msg)
                                    //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }
                                override fun onFailure(
                                    asyncActionToken: IMqttToken?,
                                    exception: Throwable?
                                ) {
                                    Log.d(this.javaClass.name, "Failed to publish message to topic")
                                }
                            })
                    } else {
                        Log.d(this.javaClass.name, "Impossible to publish, no server connected")
                    }
                }
            }
        }

        view.findViewById<Button>(R.id.right_button).setOnLongClickListener(
            OnLongClickListener {

                mAutoDecrement = true
                repeatUpdateHandler.post(RptUpdaterRigth())


                false
            })


        view.findViewById<Button>(R.id.right_button)
            .setOnTouchListener(OnTouchListener { v, event ->
                if ((event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL)
                    && mAutoDecrement
                ) {
                    mAutoDecrement = false
                }
                false
            })


        view.findViewById<Button>(R.id.back).setOnClickListener {
            findNavController().navigate(
                R.id.action_clientFragmentDcam_to_ConnectFragment
            )
        }


    }
}

