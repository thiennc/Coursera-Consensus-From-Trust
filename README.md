# Consensus From Trust
Assignments from the **Coursera course "Bitcoin and Cryptocurrency Technologies"**

You will be responsible for creating a file called **CompliantNode.java** that implements the following API:
```java
public interface Node {

// NOTE: Node is an interface and does not have a constructor.
// However, your CompliantNode.java class requires a 4 argument
// constructor as defined in the provided CompliantNode.java.
// This constructor gives your node information about the 
// simulation including the number of rounds it will run for.

/** {@code followees[i]} is true if this node follows node
  *{@code i} 
  */
  void setFollowees(boolean[] followees);

  /** initialize proposal list of transactions */
  void setPendingTransaction(Set<Transaction> pendingTransactions);

  /**
  * @return proposals to send to my followers. REMEMBER: After 
  * final round, behavior of {@code getProposals} changes and 
  * it should return the transactions upon which consensus has 
  * been reached.
  */
  Set<Transaction> sendToFollowers();

  /** receive candidates from other nodes. */
  void receiveFromFollowees(Set<Candidate> candidates);
}
```
