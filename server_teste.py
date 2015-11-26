from socket import *
import signal
from time import sleep

#Capture SIGINT for cleanup when the script is aborted
def end_listen(signal,frame):
    print "Ctrl+C captured, ending read."
    continue_listening = False
    conn.close()

print "Press Ctrl-C to stop."
continue_listening = True
signal.signal(signal.SIGINT, end_listen)
HOST = "192.168.1.179" #local host
PORT = 9090 #open port 9090 for connection
try:
    s = socket(AF_INET, SOCK_STREAM)
    s.bind((HOST, PORT))
    s.listen(1) #how many connections can it receive at one time
    print "Listening ..."
    while continue_listening:
        conn, addr = s.accept() #accept the connection
        print "Connected by: " , addr #print the address of the person connected
        data = conn.recv(1024) #how many bytes of data will the server receive
        print "Received: ", repr(data)
        if "on" in data:
            reply = "on"
        elif "off" in data:
            reply = "off"
        elif "estado" in data:
            reply = "ligado"
        else:
            reply = "oi?"
        #reply = raw_input("Reply: ") #server's reply to the client
        sleep(5)
        conn.sendall(reply)
        conn.close()
except :
    print "Deu ruim"
