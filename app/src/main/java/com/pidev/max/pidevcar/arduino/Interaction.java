package com.pidev.max.pidevcar.arduino;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by max on 19.09.16.
 */

public class Interaction extends AsyncTask<BluetoothDevice, Byte[], Void> {

    private static final UUID DEFAULT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private InteractionHandler handler;
    private ArrayList<byte[]> dataToSend;
    private OutputStream out;
    private boolean connectionIsActive;

    public Interaction(InteractionHandler handler) {
        this.handler = handler;
        dataToSend = new ArrayList<>();
    }

    public void sendData(byte[] data) {
        dataToSend.add(data);
    }

    public void disconnect() {
        connectionIsActive = false;
    }

    @Override
    protected Void doInBackground(BluetoothDevice... params) {
        BluetoothDevice device  = params[0];
        try {
            BluetoothSocket btSocket = device.createRfcommSocketToServiceRecord(DEFAULT_UUID);

            try {
                btSocket.connect();
                out = btSocket.getOutputStream();
                connectionIsActive = true;

                while (connectionIsActive) {
                    if (dataToSend.size()> 0) {
                        byte[] data = dataToSend.remove(0);

                        out.write(data);
                        out.flush();
                    }
                }

                btSocket.close();
            } catch (IOException exception) {
                exception.printStackTrace();
                btSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Byte[]... values) {
        this.handler.handleData(values[0]);
    }
}
