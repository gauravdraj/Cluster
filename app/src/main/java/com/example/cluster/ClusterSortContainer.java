package com.example.cluster;

public class ClusterSortContainer {
    private String clusterName;
    private String clusterID;

    /**
     * Constructor to help sort clusters
     * @param clusterName name of the cluster
     * @param clusterID id of the cluster (from firebase)
     */
    public ClusterSortContainer(String clusterName, String clusterID) {
        this.clusterName = clusterName;
        this.clusterID = clusterID;
    }

    public ClusterSortContainer() {
    }

    /**
     * @return name of the cluster
     */
    public String getClusterName() {
        return clusterName;
    }

    /**
     * Sets new name of the cluster
     * @param clusterName the new cluster name
     */
    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    /**
     * @return id of the cluster
     */
    public String getClusterID() {
        return clusterID;
    }

    /**
     * Sets the cluster ID
     * @param clusterID the id of the cluster
     */
    public void setClusterID(String clusterID) {
        this.clusterID = clusterID;
    }
}
