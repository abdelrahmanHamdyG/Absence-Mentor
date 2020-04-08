package com.abdelrahmanhamdy2.absencementor2.ActivitesAndFragments


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.abdelrahmanhamdy2.absencementor2.R
import com.abdelrahmanhamdy2.absencementor2.Room.DAO
import com.abdelrahmanhamdy2.absencementor2.Room.DatabaseRoom
import com.abdelrahmanhamdy2.absencementor2.Room.ModelDataRoom
import com.abdelrahmanhamdy2.absencementor2.ViewModelHelpersAdapter.RecyclerAdapterHistory
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 */
class HistoryFragment : Fragment() {

    lateinit var dao: DAO
    lateinit var recyclerHistory:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        var view=inflater.inflate(R.layout.activity_history, container, false)
        recyclerHistory=view.findViewById(R.id.recyclerHistoryActivity)
        dao= DatabaseRoom.getDataBase(requireContext()).returnDao()
        var toolbar=activity!!.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarOfFragments)

        toolbar.title="Your History"
        var arrayList: ArrayList<ModelDataRoom>
        CoroutineScope(Dispatchers.IO).launch {
            arrayList = dao.readData() as ArrayList<ModelDataRoom>
            showAdapterHistory(requireContext(),arrayList)
        }



        return view
    }

    private suspend fun showAdapterHistory(context:Context,arrayList:ArrayList<ModelDataRoom>){
        withContext(Dispatchers.Main){
            if (arrayList.size==0){
                NoHistory.visibility= View.VISIBLE

            }else{
                recyclerHistory.layoutManager=
                    LinearLayoutManager(context, RecyclerView.VERTICAL,false)
                recyclerHistory.adapter= RecyclerAdapterHistory(context,arrayList)
            }
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("MyTag","onAttach")

    }

    override fun onStart() {
        super.onStart()
        Log.i("MyTag","onStart")



    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("MyTag","onActivityCreated")

    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        Log.i("MyTag","onAttachFragment")


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("MyTag","onCreate")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MyTag","onDestroy")

    }

    override fun onDetach() {
        super.onDetach()
        Log.i("MyTag","onDetach")

    }

    override fun onDestroyView() {
        super.onDestroyView()

        Log.i("MyTag","onDestroyView")

    }


}
