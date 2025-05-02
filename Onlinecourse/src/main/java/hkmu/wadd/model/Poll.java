package hkmu.wadd.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "poll")
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    @ElementCollection
    @CollectionTable(name = "poll_options", joinColumns = @JoinColumn(name = "poll_id"))
    @Column(name = "option_text")
    private List<String> options;

    @ElementCollection
    @CollectionTable(name = "poll_votes", joinColumns = @JoinColumn(name = "poll_id"))
    @Column(name = "vote_count")
    private List<Integer> votes;

    @ManyToMany
    @JoinTable(
            name = "poll_user_votes",
            joinColumns = @JoinColumn(name = "poll_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> voters = new ArrayList<>();

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vote> votesList = new ArrayList<>();

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PollComment> comments = new ArrayList<>();



    public Poll() {}

    public Poll(String question, List<String> options, List<Integer> votes) {
        this.question = question;
        this.options = options;
        this.votes = votes;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<Integer> getVotes() {
        return votes;
    }

    public void setVotes(List<Integer> votes) {
        this.votes = votes;
    }

    public List<User> getVoters() {
        return voters;
    }

    public void setVoters(List<User> voters) {
        this.voters = voters;
    }

    public List<Vote> getVotesList() {
        return votesList;
    }

    public void setVotesList(List<Vote> votesList) {
        this.votesList = votesList;
    }


    public List<PollComment> getComments() {
        return comments;
    }

    public void setComments(List<PollComment> comments) {
        this.comments = comments;
    }

    public void addComment(PollComment comment) {
        comments.add(comment);
    }

    public void removeComment(PollComment comment) {
        comments.remove(comment);
    }

    public void addVote(int optionIndex, User user) {
        boolean hasVoted = votesList.stream()
                .anyMatch(v -> v.getUser().getId().equals(user.getId()));

        if (!hasVoted && optionIndex >= 0 && optionIndex < votes.size()) {
            votes.set(optionIndex, votes.get(optionIndex) + 1);
            voters.add(user);
            votesList.add(new Vote(user, this, optionIndex));
        }
    }
    public Vote getUserVote(User user) {
        if (user == null || votesList == null) return null;

        return votesList.stream()
                .filter(v -> v.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElse(null);
    }
    public void removeVote(User user) {

        Optional<Vote> userVote = votesList.stream()
                .filter(v -> v.getUser().getId().equals(user.getId()))
                .findFirst();

        if (userVote.isPresent()) {
            int optionIndex = userVote.get().getOptionIndex();
            votes.set(optionIndex, votes.get(optionIndex) - 1);
            votesList.removeIf(v -> v.getUser().getId().equals(user.getId()));
            voters.removeIf(u -> u.getId().equals(user.getId()));
        }
    }
    public List<Double> getVotePercentages() {
        int totalVotes = votes.stream().mapToInt(Integer::intValue).sum();
        return votes.stream().map(voteCount -> (double) voteCount / totalVotes * 100).toList();
    }

}
