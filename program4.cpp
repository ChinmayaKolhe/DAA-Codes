#include <iostream>
#include <vector>
#include <queue>
#include <climits>
#include <stack>
using namespace std;

// Dijkstra's algorithm with parent tracking
void dijkstra(int source, const vector<vector<pair<int, int>>> &graph, vector<int> &dist, vector<int> &parent) {
    int V = graph.size();
    dist.assign(V, INT_MAX);
    parent.assign(V, -1); // to store path
    dist[source] = 0;

    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<>> pq;
    pq.push({0, source});

    while (!pq.empty()) {
        int u = pq.top().second;
        int d = pq.top().first;
        pq.pop();

        if (d > dist[u])
            continue;

        for (auto &edge : graph[u]) {
            int v = edge.first;
            int w = edge.second;

            if (dist[v] > dist[u] + w) {
                dist[v] = dist[u] + w;
                parent[v] = u; // track parent
                pq.push({dist[v], v});
            }
        }
    }
}

// Function to print path from source to destination
void printPath(int dest, const vector<int> &parent) {
    stack<int> path;
    int current = dest;

    while (current != -1) {
        path.push(current);
        current = parent[current];
    }

    cout << "Path: ";
    while (!path.empty()) {
        cout << path.top();
        path.pop();
        if (!path.empty())
            cout << " -> ";
    }
    cout << "\n";
}

int main() {
    int V, E;
    cout << "Enter number of intersections (vertices): ";
    cin >> V;

    if (V <= 0) {
        cout << "Number of intersections must be positive.\n";
        return 1;
    }

    cout << "Enter number of roads (edges): ";
    cin >> E;

    if (E < 0) {
        cout << "Number of roads cannot be negative.\n";
        return 1;
    }

    vector<vector<pair<int, int>>> graph(V);
    cout << "Enter edges in format: u v w (0-based indexing, u and v are intersections, w = travel time in minutes):\n";

    for (int i = 0; i < E; i++) {
        int u, v, w;
        cin >> u >> v >> w;

        if (u < 0 || u >= V || v < 0 || v >= V || w < 0) {
            cout << "Invalid input. Node indices must be 0 to " << V - 1 << " and weight must be non-negative.\n";
            return 1;
        }

        graph[u].push_back({v, w});
        graph[v].push_back({u, w}); // undirected road
    }

    int source;
    cout << "Enter ambulance start location (source node, 0 to " << V - 1 << "): ";
    cin >> source;

    if (source < 0 || source >= V) {
        cout << "Invalid source node.\n";
        return 1;
    }

    int H;
    cout << "Enter number of hospitals: ";
    cin >> H;

    if (H <= 0 || H > V) {
        cout << "Invalid number of hospitals.\n";
        return 1;
    }

    vector<int> hospitals(H);
    cout << "Enter hospital node indices (0-based): ";

    for (int i = 0; i < H; i++) {
        cin >> hospitals[i];
        if (hospitals[i] < 0 || hospitals[i] >= V) {
            cout << "Invalid hospital node.\n";
            return 1;
        }
    }

    vector<int> dist, parent;
    dijkstra(source, graph, dist, parent);

    int minTime = INT_MAX, nearestHospital = -1;
    for (int h : hospitals) {
        if (dist[h] < minTime) {
            minTime = dist[h];
            nearestHospital = h;
        }
    }

    if (nearestHospital == -1 || minTime == INT_MAX) {
        cout << "No hospital reachable from source node " << source << ".\n";
    } else {
        cout << "Nearest hospital is at node " << nearestHospital
             << " with travel time " << minTime << " minutes.\n";
        printPath(nearestHospital, parent);
    }

    return 0;
}
