from flask import Flask, render_template,request , redirect,url_for ,make_response, Markup
import json
import hashlib
from create_db import *
from time import *

app = Flask(__name__)


# 0acRn871Qt0pD93WQg1bWVcdyJ6bofkm = Invalid Password
# Z37l1BMzLsgj7Gu1fmij3Ga9I6fRMYOB = Invalid USER
# wVV188l0l36UVpjh47yv15MYHsbF8G2S = HOME MESSAGE
# moN28Sk4IJ9facBhTdxS3xo9qFAERrjd = Bad Request



@app.route('/')
def index():
	sess= request.cookies.get('session_key') if request.cookies.get('session_key') else "wVV188l0l36UVpjh47yv15MYHsbF8G2S"
	message=""
	if "0acRn871Qt0pD93WQg1bWVcdyJ6bofkm" in sess:
		message=Markup("""<div class="alert alert-dark alert-dismissible fade show" role="alert" id="err">
	    <button type="button" class="close" data-dismiss="alert" aria-label="Close">&times;</button><center>{}</center>
	   </div>""".format("Invalid Password !!!"))
	elif "Z37l1BMzLsgj7Gu1fmij3Ga9I6fRMYOB" in sess:
		message=Markup("""<div class="alert alert-dark alert-dismissible fade show" role="alert" id="err">
	    <button type="button" class="close" data-dismiss="alert">&times;</button><center>{}</center>
	   </div>""".format("No such user found !!!"))
	elif "moN28Sk4IJ9facBhTdxS3xo9qFAERrjd" in sess:
		message=Markup("""<div class="alert alert-dark alert-dismissible fade show" role="alert" id="err">
	    <button type="button" class="close" data-dismiss="alert">&times;</button><center>{}</center>
	   </div>""".format("Bad Request !!!"))
	response = make_response(render_template('login.html',message=message))
	response.set_cookie('session_key',"wVV188l0l36UVpjh47yv15MYHsbF8G2S" )
	return response

@app.route('/update', methods=['POST'])
def update():
	data=request.get_json()
	lat=data['latitude']
	lon=data['longitude']
	userid=data['username']
	passw=data['password']
	print(userid)
	print(passw)
	conn=create_connection("firebase.db")
	if not check_user(conn,username):
		d={"error":400,"message":"No such user found."}
		return json.dumps(d)
	if update_location(conn,lat,lon,userid,passw):
		d={"error":200,"message":"Success."}
		return json.dumps(d)
	else:
		d={"error":401,"message":"Invalid Password."}
		return json.dumps(d)
	# https://www.google.com/maps/place/20.247964,+85.800741/@20.247951,85.8001988,19z/data=!4m5!3m4!7e2!8m2!3d20.2479636!4d85.8007406

@app.route("/data", methods=["GET", "POST"])
def get_data():
	if request.method == "POST":
		username = request.form["username"]

"""
@app.route('/signin', methods=['POST'])
def signin():
	req=request.get_data().decode('utf8');
	# curl -X POST -d '{"username":"SpeedX","password":"asfasfafgafg"}' http://127.0.0.1:5000/login
	data=json.loads(req)
	username=data['username']
	password=data['password']
	# password = hashlib.md5(password.encode()).hexdigest()
	conn=create_connection("firebase.db")
	if not check_user(conn,username):
		d={"error":400,"message":"No such user found."}
		return json.dumps(d)
	if not check_password(conn,username,password):
		d={"error":401,"message":"Invalid Password."}
		return json.dumps(d)
	else:
		d={"error":200,"message":"Success."}
		return json.dumps(d)
"""

@app.route('/login', methods=['POST'])
def login():
	username=request.form['uid']
	password=request.form['password']
	#password = hashlib.md5(password.encode()).hexdigest()
	conn=create_connection("firebase.db")
	response = make_response(redirect('/'))
	if not check_user(conn,username):
		d={"error":400,"message":"No such user found."}
		error_message="No such user found !!!"
		response.set_cookie('session_key',"Z37l1BMzLsgj7Gu1fmij3Ga9I6fRMYOB" )
		return response
	if not check_password(conn,username,password):
		d={"error":401,"message":"Invalid Password."}
		error_message="Invalid Password !!!"
		response.set_cookie('session_key',"0acRn871Qt0pD93WQg1bWVcdyJ6bofkm" )
		return response
	else:
		d={"error":200,"message":"Success."}
		response = make_response(redirect('/live'))
		response.set_cookie('session_key',"wVV188l0l36UVpjh47yv15MYHsbF8G2S" )
		response.set_cookie('user_id',username)
		response.set_cookie('password',password)
		return response

@app.route('/logout', methods=['GET'])
def logout():
	response = make_response(redirect('/'))
	response.set_cookie('session_key',"wVV188l0l36UVpjh47yv15MYHsbF8G2S" )
	response.set_cookie('user_id', '', expires=0)
	response.set_cookie('password', '', expires=0)
	response.set_cookie('s_lat', '', expires=0)
	response.set_cookie('s_long', '', expires=0)
	return response
	

@app.route('/live',methods=['GET'])
def live():
	if request.cookies.get('session_key') == "wVV188l0l36UVpjh47yv15MYHsbF8G2S":
		username=request.cookies.get('user_id')
		password=request.cookies.get('password')
		if username and password:
			conn=create_connection("firebase.db")
			lat,lon=fetch_location(conn,username,password)
			slat=request.cookies.get('s_lat')
			slon=request.cookies.get('s_lon')
			if not slat:
				url="https://maps.google.com/maps?q="+lat+","+lon+"&hl=en&z=14&amp;output=embed"
			else:
				url="""https://www.google.com/maps/embed?pb=!1m24!1m12!1m3!1d3742.829521227608!2d85.79599506491998!3d20.265903836416538!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!4m9!3e6!4m3!3m2!1d"""+str(slat)+"""!2d"""+str(slon)+"""!4m3!3m2!1d"""+str(lat)+"""!2d"""+str(lon)+"""!5e0!3m2!1sen!2sin!4v1581178654793!5m2!1sen!2sin"""
			# url="https://www.google.com/maps/dir/20.2658668,85.796031/20.264186,85.79793"
		else:
			response = make_response(redirect('/'))
			response.set_cookie('session_key',"moN28Sk4IJ9facBhTdxS3xo9qFAERrjd" )
			return response
	# frame=Markup("""<iframe width="100%"   height="617"   frameborder="0"   scrolling="no"   marginheight="0"  marginwidth="0"   src=\""""+url+"""\" ></iframe>""")
	iframe="""<iframe src="""+url+""" width="100%" height="625" frameborder="0" style="border:0;" allowfullscreen=""></iframe><meta http-equiv="refresh" content="10" />"""
	print(iframe)
	frame=Markup(iframe)
	response = make_response(render_template("livetrack.html",username=username,frames=frame))
	if not (slat and slon):
		response.set_cookie('s_lat',lat )
		response.set_cookie('s_lon',lon )
	return response
	# <iframe src="https://www.google.com/maps/embed?pb=!1m24!1m12!1m3!1d3742.829521227608!2d85.79599506491998!3d20.265903836416538!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!4m9!3e6!4m3!3m2!1d"""+slat+"""!2d"""+slon+"""!4m3!3m2!1d"""+clat+"""!2d"""+clon+"""!5e0!3m2!1sen!2sin!4v1581178654793!5m2!1sen!2sin" width="600" height="450" frameborder="0" style="border:0;" allowfullscreen=""></iframe>
	# return """<iframe src="https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d935.8155906753805!2d85.8001988!3d20.247951!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0x0!2zMjDCsDE0JzUyLjciTiA4NcKwNDgnMDIuNyJF!5e0!3m2!1sen!2sin!4v1581091300549!5m2!1sen!2sin" width="100%" height="100%" frameborder="0" style="border:0;" allowfullscreen=""></iframe>"""
@app.route('/locate', methods=['POST'])
def locate():
	req=request.get_data().decode('utf8');
	data=json.loads(req)
	userid=data['username']
	passw=data['password']
	conn=create_connection("firebase.db")
	if not check_user(conn,userid):
		d={"error":400,"message":"No such user found."}
		return json.dumps(d)
	else:
		lat,lon=fetch_location(conn,userid,passw)
		d={"location":"https://www.google.com/maps/place/"+lat+",+"+lon+"/"}
		return json.dumps(d)
	# https://www.google.com/maps/place/20.247964,+85.800741/@20.247951,85.8001988,19z/data=!4m5!3m4!7e2!8m2!3d20.2479636!4d85.8007406
	# http://maps.google.com/maps?q=20.247964,+85.800741
	
	
@app.route('/create', methods=['POST'])
def create():
	dat=request.get_json(force=True)
	print(dat)
	# userid='sd'
	# passw='sd'
	userid=dat['username']
	passw=dat['password']
	#passw = hashlib.md5(passw.encode()).hexdigest()
	conn=create_connection("firebase.db")
	if not check_user(conn,userid):
		create_user(conn,userid,passw)
		d={"error":200,"message":"Success."}
		return json.dumps(d)
	else:
		d={"error":400,"message":"user exists."}
		return json.dumps(d)
	if update_location(conn,lat,lon,userid,passw):
		d={"error":200,"message":"Success."}
		return json.dumps(d)
	else:
		d={"error":401,"message":"Invalid Password."}
		return json.dumps(d)
app.run(debug=True)