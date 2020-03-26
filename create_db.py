import sqlite3
from sqlite3 import Error

database = r"firebase.db"

def create_table(conn, create_table_sql):
	try:
		c = conn.cursor()
		c.execute(create_table_sql)
	except Error as e:
		print(e)
def create_connection(db_file):
	conn = None
	try:
		conn = sqlite3.connect(db_file)
	except Error as e:
		print(e)
	return conn

def create_user(conn, userid,passw):
	if check_user(conn,userid):
		return False
	cur = conn.cursor()
	sql = ''' INSERT INTO information(userid,password) VALUES(?,?) '''
	cur.execute(sql, (userid,passw))
	conn.commit()
	return cur.rowcount >=1
def check_user(conn,username):
	cur = conn.cursor()
	cur.execute("SELECT userid FROM information WHERE userid = ?", (username,))
	data=cur.fetchall()
	return len(data)>0
def check_password(conn, userid, password):
	cur = conn.cursor()
	cur.execute("SELECT userid FROM information WHERE userid = ? AND password = ? ", (userid,password))
	data=cur.fetchall()
	return len(data)>0
def update_location(conn,latitude,longitude,userid,password):
	sql = '''UPDATE information SET latitude = ? , longitude = ? WHERE userid =? and password =? '''
	cur = conn.cursor()
	cur.execute(sql, (latitude,longitude,userid,password))
	conn.commit()
	return cur.rowcount >=1
def fetch_location(conn,userid,password):
	sql = '''SELECT latitude,longitude FROM information WHERE userid =? and password =? '''
	cur = conn.cursor()
	cur.execute(sql, (userid,password))
	data=cur.fetchall()
	return (data[0][0],data[0][1])
def main():
 
	sql_create_info_table = """ CREATE TABLE IF NOT EXISTS information (
										id integer PRIMARY KEY,
										userid text NOT NULL,
										password text NOT NULL,
										latitude text DEFAULT "20.248877",
										longitude text DEFAULT "85.801364"
									); """
	# create a database connection
	conn = create_connection(database)
 
	# create tables
	if conn is not None:
		# create projects table
		create_table(conn, sql_create_info_table)
 
	else:
		print("Error! cannot create the database connection.")

main()