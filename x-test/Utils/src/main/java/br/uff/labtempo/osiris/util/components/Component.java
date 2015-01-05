/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.util.components;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public abstract class Component {

    /**
     * Launch a new activity. You will not receive any information about when
     * the activity exits.
     *
     * Note that if this method is being called from outside of an Component
     * Context, then the Intent must include the FLAG_ACTIVITY_NEW_TASK launch
     * flag. This is because, without being started from an existing Activity,
     * there is no existing task in which to place the new activity and thus it
     * needs to be placed in its own separate task.
     *
     * @throws ComponentInitializationException
     */
    public void start() throws ComponentInitializationException {
        try {
            onCreate();
            onStart();
        } catch (Exception e) {
            finish();
            throw new ComponentInitializationException(e);
        }
    }

    /**
     * Call this when your activity is done and should be closed. The
     * ActivityResult is propagated back to whoever launched you via
     * onActivityResult().
     */
    public void finish() {
        onPause();
        onStop();
        onDestroy();
    }

    /**
     * Called when a component is starting. This is where you should do all of
     * your normal static set up. Always followed by onStart().
     *
     * @throws ComponentInitializationException
     */
    protected abstract void onCreate() throws ComponentInitializationException;

    /**
     * Called when the activity is becoming visible to the user.
     *
     * @throws ComponentInitializationException
     */
    protected void onStart() throws ComponentInitializationException{};

    /**
     * Called when the system is about to start resuming a previous activity.
     * This is typically used to commit unsaved changes to persistent data, stop
     * animations and other things that may be consuming CPU, etc.
     * Implementations of this method must be very quick because the next
     * activity will not be resumed until this method returns.
     */
    protected void onPause() {
    }

    /**
     * Called when the component is no longer visible to the user, because
     * another activity has been resumed and is covering this one. This may
     * happen either because a new activity is being started, an existing one is
     * being brought in front of this one, or this one is being destroyed.
     */
    protected void onStop() {
    }

    /**
     * The final call you receive before your activity is destroyed. This can
     * happen either because the activity is finishing (someone called finish()
     * on it, or because the system is temporarily destroying this instance of
     * the activity to save space. You can distinguish between these two
     * scenarios with the isFinishing() method.
     */
    protected void onDestroy() {
    }
}
