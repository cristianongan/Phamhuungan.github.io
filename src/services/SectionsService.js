import axios from 'axios'
import {endPoint,KeyWord} from '../constants.js'
import authHeader from './authHeadersService.js'

export default {
    getSecs(){
        
        return axios.get(endPoint.sections,{headers:authHeader.getAuthHeader()})
        .then(response=>{
            let list = response.data.result;
            let state = [];
            this.setupList(list,state,null)

            

            return state
           
        }).catch(error=>{
            alert(error)
        })
        
    },

    addSec(sec){
        
        return axios.post(endPoint.addSection,sec,{headers:authHeader.getAuthHeader()})
    },

    updateSec(sec){
        return axios.put(endPoint.updateSection,sec,{headers:authHeader.getAuthHeader()})
    },

    deleteSec(id){
        return axios.delete(endPoint.deleteSec+KeyWord.slash+id)
    },
    setupList(list,state,con){
    if(list.length>0){
        
        if(state.length == 0){
            for(let i = 0; i< list.length ; i++){
                    if(list[i].parent == con){
                        state.push({
                            object:list[i],
                            childrens:[]
                        });
                        list.splice(i,1);
                        i--;
                    }   
            }
            

            for(let item in state){
                this.setupList(list,state,state[item].object.id)
                
            }
        }
        else {
            
            for( let i=0; i<state.length ; i++){
                if(state[i].object.id === con){
                    for(let y=0;y<list.length;y++){


                        if(list[y].parent.id==con){
                            

                            state[i].childrens.push({
                                object:list[y],
                                childrens:[]
                            });

                            let id = list[y].id
                            list.splice(y,1);
                            y--;

                            this.setupList(list,state[i].childrens,id)
                        }
                    }
                    break;
                }
               
            }
        }
    }
    
}
    
}