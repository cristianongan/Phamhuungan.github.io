  const domain ="http://localhost:8081"
  
  const endPoint ={

    auth : domain+"/api/login",
    sections: domain+"/api/sec",
    users: domain+"/api/listUsers",
    usersf: domain+"/api/listUsersf",
    updateUser: domain+"/api/update",
    deleteUser: domain+"/api/delete",
    role: domain+"/api/role",
    res: domain+"/api/res",
    updateSection: domain+"/api/update/sec",
    addSection: domain+"/api/add/sec",
    deleteSec: domain+"/api/delete/sec",
    personalInformation: domain+"/api/personalInfomation",
    updatePassword: domain+"/api/updatePassword"
}

const Attr = {
    token:"token",
    role:"tole"
}

const KeyWord={
  slash:"/",
  authHeader:"authorization"
}

const Code = {
  ok:"OK",
  WrongUserName:"E01",
  WrongPassword:"E02",
  Dup:"E03",
  ServerError:"ES",
  Error:"E"
}

export {endPoint ,Attr ,Code ,KeyWord}