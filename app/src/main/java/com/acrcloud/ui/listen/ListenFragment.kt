package com.acrcloud.ui.listen


import android.arch.lifecycle.ViewModelProviders
import android.databinding.Observable
import android.os.Bundle
import android.view.View
import com.acrcloud.rec.sdk.ACRCloudClient
import com.acrcloud.rec.sdk.ACRCloudConfig
import com.acrcloud.rec.sdk.ACRCloudResult
import com.acrcloud.rec.sdk.IACRCloudResultWithAudioListener
import com.acrcloud.ui.BR
import com.acrcloud.ui.R
import com.acrcloud.ui.base.BaseFragment
import com.acrcloud.ui.databinding.FragmentListenBinding
import com.acrcloud.ui.edit.MainNavigator
import kotlinx.android.synthetic.main.fragment_listen.*
import java.util.jar.Manifest


class ListenFragment : BaseFragment<FragmentListenBinding, ListenMusicViewModel>() {

    private var mClient: ACRCloudClient? = null
    private var mConfig: ACRCloudConfig? = null
    private var initState: Boolean = false

    override val bindingVariable: Int
        get() = BR.viewModel

    override val viewModel: ListenMusicViewModel
        get() {
            val viewModel = ViewModelProviders.of(activity!!).get(ListenMusicViewModel::class.java)
            viewModel.navigator = activity as MainNavigator?
            return viewModel
        }


    override val layoutId: Int
        get() = R.layout.fragment_listen

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.mConfig = ACRCloudConfig()
        this.mConfig!!.acrcloudResultWithAudioListener = object : IACRCloudResultWithAudioListener {
            override fun onVolumeChanged(p0: Double) {

            }

            override fun onResult(result: ACRCloudResult?) {
                println(result)
            }

        }


        this.mConfig!!.context = context;
        this.mConfig!!.host = "identify-eu-west-1.acrcloud.com";
        //this.mConfig!!.dbPath = path; // offline db path, you can change it with other path which this app can access.
        this.mConfig!!.accessKey = "37a24216f7bdbfd272dab7035927e4cd";
        this.mConfig!!.accessSecret = "pfVkL0Vg4frc3Wk92qoqvfGgT8u5nQLLuS3AfHNK";
        this.mConfig!!.protocol = ACRCloudConfig.ACRCloudNetworkProtocol.PROTOCOL_HTTP; // PROTOCOL_HTTPS
        this.mConfig!!.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_REMOTE;
        //this.mConfig.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_LOCAL;
        //this.mConfig.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_BOTH;


        this.mClient = ACRCloudClient();
//         If reqMode is REC_MODE_LOCAL or REC_MODE_BOTH,
//         the function initWithConfig is used to load offline db, and it may cost long time.
        this.initState = this.mClient!!.initWithConfig(this.mConfig)
        if (this.initState) {
            this.mClient!!.startPreRecord(3000); //start prerecord, you can call "this.mClient.stopPreRecord()" to stop prerecord.
        }

        toggleButton.listening.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (toggleButton.listening.get()) {
                    mClient!!.startRecognize()
                } else mClient!!.stopRecordToRecognize()
            }
        })

        if(!baseActivity!!.hasPermission(android.Manifest.permission.RECORD_AUDIO)){
            baseActivity!!.requestPermissionsSafely(arrayOf(android.Manifest.permission.RECORD_AUDIO),1)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (this.mClient != null) {
            this.mClient!!.release()
            this.mClient = null
        }
    }
}

