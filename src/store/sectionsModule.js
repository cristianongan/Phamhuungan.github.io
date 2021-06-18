import secsService from '../services/SectionsService.js'

export default{
    namespaced:true,
    state:{
        list:[],
        section:{}
        
    },

    methods:{
            
       
    },
    mutations:{
        updateList(state,listSecs){
            // let list = listSecs;

            // state.list = [];

            // this.setupList(list,state,null);

           
            state.list=listSecs
            
        },
        updateSection(state, section){
            state.section = section;
        }


    },
    actions:{
       update(context){

        let listSecs =[]
        secsService.getSecs().then(data =>{
            listSecs = data
            context.commit('updateList',listSecs)
            console.log(listSecs)
        })
        
       },
       updateSection(context, section){
        context.commit('updateSection',section)
       }
        
    },
    getters:{
        getListSecs(state){
            return state.list
        },
        getSecByid(state,id){
            for(let x in state.list){
                if(x.id == id){
                    return x
                }
            }
            return null;
        },
        getSectionActived(state){
            return state.section
        }
    }
}