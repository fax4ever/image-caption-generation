import os
from flask import Flask, flash, request, redirect, url_for
from werkzeug.utils import secure_filename
from markupsafe import escape
from caption_service import openAndGenerate
from message_service import sendMessageWithRetry

UPLOAD_FOLDER = '/app/static'

app = Flask(__name__)
app.secret_key = "super secret key"
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

@app.route('/caption/image/<path:path>')
def static_file(path):
    return app.send_static_file(path)

@app.route('/caption/new-image/<username>', methods=['GET', 'POST'])
def upload_file(username):
    username = escape(username)
    if request.method == 'POST':
        if 'file' not in request.files:
            flash('No file part')
            return redirect(request.url)
        file = request.files['file']
        if file.filename == '':
            flash('No selected file')
            return redirect(request.url)
        directory = os.path.join(UPLOAD_FOLDER, username)
        if not os.path.exists(directory):
            os.makedirs(directory)
        filename = secure_filename(file.filename)
        fullname = os.path.join(directory, filename)
        file.save(fullname)
        flash('File saved:' + filename)
        caption = openAndGenerate(fullname)
        sendMessageWithRetry(username, filename, caption)
        flash('Generated caption:' + caption)
        return caption
    return '''
    <!doctype html>
    <title>Upload new File</title>
    <h1>Upload new File</h1>
    <form method=post enctype=multipart/form-data>
      <input type=file name=file>
      <input type=submit value=Upload>
    </form>
    '''

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8000)