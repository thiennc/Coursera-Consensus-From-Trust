import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

/* CompliantNode refers to a node that follows the rules (not malicious)*/
public class CompliantNode implements Node {
    private double _p_graph, _p_malicious, _p_txDistribution;
    private boolean[] _followees, _blacklists;
    private Set<Transaction> _consensusTrans, _pendingTrans = new HashSet<>();
    private int _numRounds;
    private int _currentRound, _oldRound = 0;

    public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
        this._p_graph = p_graph;
        this._p_malicious = p_malicious;
        this._p_txDistribution = p_txDistribution;
        this._numRounds = numRounds;
    }

    public void setFollowees(boolean[] followees) {
        this._followees = followees;
        this._blacklists = new boolean[followees.length];
        Arrays.fill(this._blacklists,Boolean.FALSE);
    }

    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        this._pendingTrans = pendingTransactions;
        this._consensusTrans = pendingTransactions;
    }

    public Set<Transaction> sendToFollowers() {
        Set<Transaction> Txs = new HashSet<>();
        if (_currentRound == _numRounds) {
            Txs = _consensusTrans;
        } else if (_currentRound < _numRounds) {
            Txs.addAll(_pendingTrans);
            _oldRound = _currentRound;
        }
        return Txs;
    }

    public void checkMalicious(Set<Candidate> candidates) {
        Set<Integer> senders = candidates.stream().map(c -> c.sender).collect(toSet());
        for (int i = 0; i < _followees.length; i++) {
            if (_followees[i] && !senders.contains(i)) {    //node might be functionally dead and never actually broadcast any transactions
                _blacklists[i] = true;
            }
        }
    }

    public void receiveFromFollowees(Set<Candidate> candidates) {
        this._currentRound++;
        if (_currentRound >= _numRounds-1) {
            return;
        }
        if (_oldRound > 0 && _currentRound > _oldRound) {
            _pendingTrans.clear();
        }
        checkMalicious(candidates);
        for (Candidate c : candidates) {
            if (!_consensusTrans.contains(c.tx) && !_blacklists[c.sender]) {
                _consensusTrans.add(c.tx);
                _pendingTrans.add(c.tx);
            }
        }
    }
}
