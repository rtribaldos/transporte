package com.digitaldoc.model;


/* Copyright (c) 2009 Google Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION) 
public class AnticipoFile implements Serializable{

private static final long serialVersionUID = 1L;

@PrimaryKey
 @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
 private Key key;

 @Persistent
 private User owner;

 @Persistent
 private BlobKey blob;

 @Persistent
 private Date creation;

 @Persistent
 private String contentType;

 @Persistent
 private String filename;

 @Persistent
 private long size;

 @Persistent
 private String title;

 @Persistent
 private String description;
 
 @Persistent(mappedBy = "fichero")
 @Element(dependent = "true")
 private Anticipo anticipo;
 
 private static final List<String> IMAGE_TYPES = Arrays.asList("image/png",
   "image/jpeg", "image/tiff", "image/gif", "image/bmp");

 
 
public AnticipoFile(User owner, BlobKey blob, Date creation, String contentType, String filename, long size,
		String title, String description, Anticipo anticipo) {
	super();
	this.owner = owner;
	this.blob = blob;
	this.creation = creation;
	this.contentType = contentType;
	this.filename = filename;
	this.size = size;
	this.title = title;
	this.description = description;
	this.anticipo = anticipo;
}

public BlobKey getBlob() {
	return blob;
}

public void setBlob(BlobKey blob) {
	this.blob = blob;
}

public Date getCreation() {
	return creation;
}

public void setCreation(Date creation) {
	this.creation = creation;
}

public Anticipo getAnticipo() {
	return anticipo;
}

public void setAnticipo(Anticipo anticipo) {
	this.anticipo = anticipo;
}

public void setKey(Key key) {
	this.key = key;
}

public void setOwner(User owner) {
	this.owner = owner;
}

public void setContentType(String contentType) {
	this.contentType = contentType;
}

public void setFilename(String filename) {
	this.filename = filename;
}

public void setSize(long size) {
	this.size = size;
}

public void setTitle(String title) {
	this.title = title;
}

public void setDescription(String description) {
	this.description = description;
}

public Key getKey() {
   return key;
 }

 public User getOwner() {
   return owner;
 }

 public Date getCreationTime() {
   return creation;
 }
     
 public String getDescription() {
   return description;
 }

 public String getTitle() {
   return title;
 }

 public String getFilename() {
   return filename;
 }
     
 public long getSize() {
   return size;
 }

 public String getContentType() {
   if (contentType ==  null) {
     return "text/plain";
   }
   return contentType;
 }

 public String getURLPath() {
   String key = blob.getKeyString();
   return "/resource?key=" + key;
 }

 public String getDisplayURL() {
   String key = blob.getKeyString();
   return "/serve?blob-key=" + key;
 }

 public boolean isImage() {
   return IMAGE_TYPES.contains(getContentType());
 }
 
}
