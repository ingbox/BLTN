package kr.ac.kpu.worldcup

import android.os.Parcel
import android.os.Parcelable

// 앱을 만들다 보면 인텐트를 통해 단순히 String, int, boolean 같은 기본 타입 뿐
// 아니고 커스텀 클래스나 오브젝트를 다른 컴포넌트에 전달해 줘야 할 경우가 많다.
// 그 경우 단순히 그냥 인텐트에 putExtra() 로는 넣어줄 수가 없다.
// 안드로이드에서는 그런 경우를 위해 자바의 Serialization 개념과 유사한 Parcelable이라는 클래스가 있다.


class Space(val id: String?, val num: Int) :Parcelable
{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt()
    ) {
        //
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeInt(num)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Space> {
        override fun createFromParcel(parcel: Parcel): Space {
            return Space(parcel)
        }

        override fun newArray(size: Int): Array<Space?> {
            return arrayOfNulls(size)
        }
    }
}