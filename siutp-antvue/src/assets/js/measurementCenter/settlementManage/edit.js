export default {
    props: {
        text: String
    },
    data() {
        return {
            value: this.text,
            editable: false,
        }
    },
    methods: {
        handleChange(e) {
            const value = e.target.value;
            this.value = value;
        },
        check() {
            this.editable = false;
            this.$emit('change', this.value);
        },
        edit() {
            this.editable = true;
        },
    },
}