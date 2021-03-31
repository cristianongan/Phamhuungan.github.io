package com.viettel.automl.dto.response;

import org.apache.hadoop.fs.FileStatus;

public class FileInfo {
	private String path;
	private long length;
	private boolean isdir;
	private short block_replication;
	private long blocksize;
	private long size;
	private long modification_time;
	private long access_time;
	private String permission;
	private String owner;
	private String group;

	public FileInfo() {
		this(null, 0L, false, (short) 0, 0L, 0L, 0L, 0L, null, null, null);
	}

	public FileInfo(String path, long length, boolean isdir, short block_replication, long blocksize, long size,
			long modification_time, long access_time, String permission, String owner, String group) {

		this.path = path;
		this.length = length;
		this.isdir = isdir;
		this.block_replication = block_replication;
		this.blocksize = blocksize;
		this.size = size;
		this.modification_time = modification_time;
		this.access_time = access_time;
		this.permission = permission;
		this.owner = owner;
		this.group = group;
	}

	public FileInfo(FileStatus fs) {
		this.path = fs.getPath().toString();
		this.length = fs.getLen();
		this.isdir = fs.isDirectory();
		this.block_replication = fs.getReplication();
		this.blocksize = fs.getBlockSize();
		this.size = 0L;
		this.modification_time = fs.getModificationTime();
		this.access_time = fs.getAccessTime();
		this.permission = fs.getPermission().toString();
		this.owner = fs.getOwner();
		this.group = fs.getGroup();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public boolean isIsdir() {
		return isdir;
	}

	public void setIsdir(boolean isdir) {
		this.isdir = isdir;
	}

	public short getBlock_replication() {
		return block_replication;
	}

	public void setBlock_replication(short block_replication) {
		this.block_replication = block_replication;
	}

	public long getBlocksize() {
		return blocksize;
	}

	public void setBlocksize(long blocksize) {
		this.blocksize = blocksize;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getModification_time() {
		return modification_time;
	}

	public void setModification_time(long modification_time) {
		this.modification_time = modification_time;
	}

	public long getAccess_time() {
		return access_time;
	}

	public void setAccess_time(long access_time) {
		this.access_time = access_time;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
}
