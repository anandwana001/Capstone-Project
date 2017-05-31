package and.com.comicoid.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 10-10-2016.
 */

public class Image implements Parcelable{

    private String name;
    private String thumbnail;
    private String description;
    private int id;

    public Image() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public String getDescription() {
        return this.description;
    }

    public int getId() {
        return this.id;
    }

    public static Parcelable.Creator<Image> getCREATOR() {
        return Image.CREATOR;
    }

    public Image(Parcel in) {
        name = in.readString();
        thumbnail = in.readString();
        description = in.readString();
        id = in.readInt();
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(thumbnail);
        dest.writeString(description);
        dest.writeInt(id);
    }
}
