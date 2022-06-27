package com.example.myapplication.attachments;

import com.example.myapplication.R;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Varun John on 12 Feb, 2020
 * Github : https://github.com/varunjohn
 */
public class AttachmentOption implements Serializable {

    private int id;
    private String title;
    private int resourceImage;

    public static final byte DOCUMENT_ID = 101;
    public static final byte CAMERA_ID = 102;
    public static final byte GALLERY_ID = 103;
    public static final byte AUDIO_ID = 104;
    public static final byte LOCATION_ID = 105;
    public static final byte CONTACT_ID = 106;


    public static List<AttachmentOption> getDefaultList() {
        List<AttachmentOption> attachmentOptions = new ArrayList<>();
        attachmentOptions.add(new AttachmentOption(DOCUMENT_ID, "Document", R.drawable.ic_doc__1_));
        attachmentOptions.add(new AttachmentOption(CAMERA_ID, "Camera", R.drawable.ic_cmera));
        attachmentOptions.add(new AttachmentOption(GALLERY_ID, "Gallery", R.drawable.ic_gallery));

        return attachmentOptions;
    }

    public AttachmentOption(int id, String title, int resourceImage) {
        this.id = id;
        this.title = title;
        this.resourceImage = resourceImage;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getResourceImage() {
        return resourceImage;
    }
}
