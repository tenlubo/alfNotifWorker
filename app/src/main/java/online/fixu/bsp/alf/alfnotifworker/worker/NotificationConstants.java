package online.fixu.bsp.alf.alfnotifworker.worker;

interface NotificationConstants {

    // Name of Notification Channel for verbose notifications of background work
    CharSequence VERBOSE_NOTIFICATION_CHANNEL_NAME =
            "Verbose WorkManager Notifications";
    String VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
            "Shows notifications whenever work starts";

    String CHANNEL_ID = "VERBOSE_NOTIFICATION" ;
    CharSequence NOTIFICATION_TITLE = "POZOOOOOR";

    String NOTIFICATION_NEW_TASK_MESSAGE = "New Bike Fixu Task";

    int NOTIFICATION_ID = 777;
    long DELAY_TIME_MILLIS = 5000;




}
