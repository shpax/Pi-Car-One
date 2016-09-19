package com.pidev.max.pidevcar.arduino;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import java.util.Set;

public class InteractionAdapter {

    public static final int BLUETOOTH_ENABLE_INTENT = 110;

    private BluetoothAdapter btAdapter;

    InteractionAdapter(Activity activity) throws Exception {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) throw new Exception("no BT device available");
        if (!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, BLUETOOTH_ENABLE_INTENT);
        }
    }

    public Set<BluetoothDevice> getPairedDevices() {
        return btAdapter.getBondedDevices();
    }

    public Interaction connect(BluetoothDevice device, InteractionHandler handler) {
        Interaction interaction = new Interaction(handler);
        interaction.execute(device);
        return interaction;
    }
}
