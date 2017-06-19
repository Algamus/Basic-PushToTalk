console.log("TheLanPttServer");

var TcpNetwork = require('net');
var UdpNetwork = require("dgram");


var TcpWPort = 1234;//handshake icin login ve register W:welcomeport
var TcpB1Port = 1235;//Devamlı baglantı icin bridge1
var TcpB2Port = 1236;//Devamplı baglantı icin bridge2
var UdpSport = 1237;//Sender
//var UdpRPort = 1238;//Receiver

var Clients = [];
var ClientLUdp = [];
var ClientSUdp=null;
var TotalClients = 0;
var GlobalWTS = true;
var startnumber = 1;
var startsocket; //for speaker socket b1

var waitstuck = 0;


var url = 'mongodb://127.0.0.1:27017/test';

var MongoClient = require('mongodb').MongoClient, format = require('util').format;
var assert = require('assert');
var ObjectId = require('mongodb').ObjectID;


MongoClient.connect(url, function (err, db) {

    if (err) {
        console.log('server is not connected');
        throw err;
    } else {
        console.log('MongoDb is Ready');
    }
    db.close();
});


///
function OnlineList(username ,SocketTcpS , SocketTcpR) {
    this.username = username;
    this.SocketTcpS = SocketTcpS;
    this.SocketTcpR = SocketTcpR;
}

function Udpconnection(username, Socket, ip, port) {
    this.username = username;
    this.Socket = Socket;
    this.ip = ip;
    this.port = port;
}
//////////////////////////////////////////////////////////
function AddUdpList(u,s,i,p) {
    ClientLUdp.push(new Udpconnection(u,s,i,p));
}
function AddUdpSender(u, s, i, p) {
    ClientSUdp = new Udpconnection(u,s,i,p);
}

function RemoveUdpList() {
    ClientSUdp = null;//.splice(0,1); //= null;
    ClientLUdp.splice(0, ClientLUdp.length);
}
//////////////////////////////////////////////////////////
function AddListUsername(username) {
    Clients.push(new OnlineList(username, null, null));
    TotalClients++;
}
function AddListSocketTcpS(username,stcps) {
    Clients.forEach(function (Client) {
        if (Client.username.indexOf(username) > -1) {
            Client.SocketTcpS = stcps;
            return;
        }
    });
}
function AddListSocketTcpR(username, stcpr) {
    Clients.forEach(function (Client) {
        if (Client.username.indexOf(username) > -1) {
            Client.SocketTcpR = stcpr;
            return;
        }
   });
}
function RemoveListbyUsername(username) {
    var count = 0
    Clients.forEach(function (Client) {
        if (Client.username.indexOf(username) > -1) {
            Clients.splice(count, 1);
            TotalClients--;
            return;
        }
        count++;
    });

}
///////////////////////////////////////////////////////
function inList(username) {
    Clients.forEach(function (Client) {
        if (Client.username.indexOf(username) > -1) {
            return 1;
        }
    });
    return 0;
}
function PrintList() {
    console.log('OnlineList: ' + TotalClients +'\n');
    Clients.forEach(function (Client) {
        console.log(Client.username + "\n");
    });
}



////////////////////////////////////////////////////////////////
function BroadcastbySocketTcpS(data, stcps) {
    Clients.forEach(function (Client) {
        if (Client.SocketTcpS == stcps) {
            
        } else {
            if (Client.SocketTcpR !=null) {
                Client.SocketTcpR.write(data);
            }
        }
    });
}

function BroadcastUdp(data) {
    ClientLUdp.forEach(function (Client) {

        Client.Socket.send(data, Client.port, Client.ip, (err) => { });
    });
}
////////////////////////////////////////////////////////////////////////////////////////////mongodb functions

function loginMD(username, password,s) {
    MongoClient.connect(url, function (err, db) {
        assert.equal(null, err);
        var cursor = db.collection('testuser2').find({ "username": username, "password": password }).count().then(function (numItems) {
            console.log(numItems);
            if (numItems > 0) {
                //var
                s.write('OK\n');
                //console.log(username + ' ' + password + ' var');
            } else {
                //yok
                s.write('NOK\n');
                //console.log(username + ' ' + password + ' yok');
            }
           // console.log(s);
            callback(numItems);
        });
        db.close();
    });
}

function registerMD(username,password,s) {
    MongoClient.connect(url, function (err, db) {
        assert.equal(null, err);
        var cursor = db.collection('testuser2').find({ "username": username }).count().then(function (numItems) {
            console.log(numItems);
            if (numItems > 0) {
                //var
                s.write('CLONE\n');
                console.log(username + ' ' + password + ' nope');
            } else {
                //yok
                s.write('OK\n');
                console.log(username+' '+ password+' ekledim');
                insertMD(username, password);
                
            }
           // console.log(s);
            callback(numItems);
        });
        db.close();
    });
   
    
}


function insertMD(username,password) {
    
    MongoClient.connect(url, function (err, db) {
        assert.equal(null, err);
        db.collection('testuser2').insertOne({
            "username": username,
            "password": password
        }, function (err, result) {
            assert.equal(err, null);
            console.log(username + " registered to mongodb.");
            
        });
        db.close();
    });

}





////////////////////////////////////////////////////////////////TCP Welcome server (regster ve login isine bakıyor)
var TcpWServer = TcpNetwork.createServer(function (socket) {

    

    socket.on('data', function (data) {//incoming Data
        console.log(socket.remoteAddress + ":" + socket.remotePort);

        var temp = data + '';//gelen dataya '' ekle gidenede '/n'
        var temp2 = temp.split('@');
        //check temp2[1] username and temp2[2] password
        if (temp2[0].indexOf('Login') > -1) {
            //database check
             //check temp2[1] username and temp2[2] password

            loginMD(temp2[1], temp2[2], socket);

        } else if (temp2[0].indexOf('Register') > -1) {
            //database check
             //check temp2[1] username and temp2[2] password
            registerMD(temp2[1],temp2[2],socket);

        }else{ }
    
    });
    socket.on('end', function () {//Leave
    });
    socket.on('disconnect', function () {
    });
}).listen(TcpWPort);
console.log("TcpWelcomeServer is listening");
//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////Tcp Bridge1
var TcpB1Server = TcpNetwork.createServer(function (socket) {
    
    socket.on('data', function (data) {//incoming Data

        var temp = data + '';//gelen dataya ''   /////    ekle gidenede '/n'
        var temp2 = temp.split('@');
        if (temp2[0].indexOf('ConnectS') > -1) {
            AddListUsername(temp2[1]);
            socket.name = temp2[1];
            AddListSocketTcpS(temp2[1], socket);
        } else if (temp2[0].indexOf('WTSPEAK') > -1) {
            //**
            
            if (GlobalWTS) {
                GlobalWTS = false;
                socket.write('OKWAIT\n');
                startsocket = socket;
                if (TotalClients <= 1) {
                    startsocket.write('START\n');
                } else {
                    BroadcastbySocketTcpS('LISTEN\n', socket);
                }
            } else {
                socket.write('NOK\n');
            }
            
        } else if (temp2[0].indexOf('STOPSPEAKING') > -1){
            //**
            GlobalWTS = true;
            BroadcastbySocketTcpS('DONE\n', socket);
            RemoveUdpList();
        } else {
            //socket.close();
        }

    });
    socket.on('error', function (err) {
        var name = socket.name;
        console.error('B1:' + name + ' ' + err);
        if (err.code.indexOf('ECONNRESET')>-1) {
            RemoveListbyUsername(name);

        }

    });
    socket.on('end', function () {//Leave
        
        RemoveListbyUsername(socket.name);
        if (!GlobalWTS && socket == startsocket) {
            GlobalWTS = true;
            BroadcastbySocketTcpS('DONE\n', null);
            RemoveUdpList();
        }
    });
    socket.on('disconnect', function () {
        //RemoveListbyUsername(socket.name);
    });
}).listen(TcpB1Port);
console.log("TcpBridge1Server is listening");
//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////Tcp Bridge2
var TcpB2Server = TcpNetwork.createServer(function (socket) {

    socket.on('data', function (data) {//incoming Data
        
        var temp = data + '';//gelen dataya ''   /////    ekle gidenede '/n'
        var temp2 = temp.split('@');
        if (temp2[0].indexOf('ConnectR') > -1) {
            socket.name = temp2[1];
            AddListSocketTcpR(temp2[1], socket);
        } else if (temp2[0].indexOf('LISTENOK') > -1) {
            startnumber++;
            //console.log("girdi....");
            if (startnumber >= TotalClients) {
                startnumber = 1;
                //console.log("genegirdi...");
                startsocket.write('START\n');
            }
        } else {
            //socket.close();
        }

    });
    socket.on('error', function (err) { console.error(err) });
    socket.on('end', function () {
    });
    socket.on('disconnect', function () {
    });
}).listen(TcpB2Port);
console.log("TcpBridge2Server is listening");
//////////////////////////////////////////////////////////////////////////////
var UdpServer = UdpNetwork.createSocket("udp4");

UdpServer.on("message", function (msg, rinfo) {
        
        //console.log("server got: " + msg + " from " + rinfo.address + ":" + rinfo.port + " size of " + rinfo.size);
        var temp = msg + '';//gelen dataya ''   /////    ekle gidenede '/n'
        var temp2 = temp.split('@');

        if (temp2[0].indexOf('S') > -1 && temp.length > 1 && temp2[0].length == 1) {
            console.log('??' + temp2[1] + 'sender');
           
            if (ClientSUdp == null) {
                
                AddUdpSender(temp2[1], UdpServer, rinfo.address, rinfo.port);
            }
            //UdpServer.send('return s ', rinfo.port, rinfo.address, (err) => { UdpServer.close });
        } else if (temp2[0].indexOf('L') > -1 && temp.length > 1 && temp2[0].length == 1) {
            AddUdpList(temp2[1], UdpServer, rinfo.address, rinfo.port);
            console.log('??' + temp2[1] + 'receiver');
            //UdpServer.send('return l ', rinfo.port, rinfo.address, (err) => { UdpServer.close });
        } else {
            //UdpServer.send(' the message ', rinfo.port, rinfo.address, (err) => { UdpServer.close });
            if (ClientSUdp != null) {
                
                if (ClientSUdp.Socket == UdpServer && ClientSUdp.ip.indexOf(rinfo.address) == 0 && ClientSUdp.port == rinfo.port) {
                    console.log("ses geliyor:" + msg.length);
                    BroadcastUdp(msg);      
                }
            } else {
                console.log('bitti');
            }
        }
    });
UdpServer.on("listening", function () {
        console.log("UdpServer is listening");
    });
UdpServer.bind(UdpSport);

//////////////////////////////////////////////////////////////////////////////////////
setInterval(function () {
    PrintList();
    if (!GlobalWTS) {//wiat signal düzelticisi
        waitstuck++;
        if ( waitstuck > 3) {
            console.log("stuck...........................................");
            startsocket.write('START\n');
            startnumber = 1;
            waitstuck = 0;
        }
    }
}, 1000);
//////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////