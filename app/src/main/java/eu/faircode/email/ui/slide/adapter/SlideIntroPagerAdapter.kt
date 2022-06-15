package eu.faircode.email.ui.slide.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import eu.faircode.email.data.model.Slide
import eu.faircode.email.R

import kotlinx.android.synthetic.main.item_slide_intro.view.*

class SlideIntroPagerAdapter(
    private val context: Context
) : PagerAdapter() {

    private val list: ArrayList<Slide> = arrayListOf()

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_slide_intro, null)

        with(view) {
            img_slide.setImageResource(list[position].image)
            tv_title.text = list[position].title
            tv_desc.text = list[position].desc
        }

        container.addView(view)

        return view
    }

    fun addSlide(slide: Slide) {
        this.list.add(slide)
    }
}
